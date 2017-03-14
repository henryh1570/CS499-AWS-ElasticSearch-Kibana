package esmaster.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class App {

	/**
	 * Load strings from a file into an array list.
	 * 
	 * @param fileName
	 * @return String ArrayList
	 */
	public static ArrayList<String> loadList(String fileName) {
		try {
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			ArrayList<String> list = new ArrayList<String>();
			String next = bufferedReader.readLine();
			while (next != null) {
				list.add(next);
				next = bufferedReader.readLine();
			}
			bufferedReader.close();
			fileReader.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write an array list to a file.
	 * 
	 * @param List
	 * @param fileName
	 * @return
	 */
	public static boolean writeListToFile(ArrayList<String> data, String fileName) {
		try {
			File f = new File(fileName);
			if (f.exists()) {
				return false;
			} else {
				PrintWriter p = new PrintWriter(f);
				p.write("[");
				for (int i = 0; i < data.size() - 1; i++) {
					p.write(data.get(i) + ",\n");
				}
				p.write(data.get(data.size() - 1) + "]\n");
				p.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Retrieve Youtube's trending videos information and output to JSON in a list.
	public static ArrayList<String> getTrendingData(String url) {
		GsonBuilder gs = new GsonBuilder();
		gs.disableHtmlEscaping();
		Gson g = gs.create();
		
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			URL html = new URL(url);
			Document doc = Jsoup.parse(html, 10000);
			Elements feeds = doc.select(".yt-lockup-content");
			// For each video
			for (Element element : feeds) {
				String title = (element.select("a").attr("title")); //Video Title
				String author = (element.select(".yt-lockup-byline").text()); // Uploader
				String dateViews = (element.select(".yt-lockup-meta-info").text()); // Date + view count
				int views = getViews(dateViews);
				String date = getDate(dateViews);
				long id = getId(title, author);
				list.add(g.toJson(new Video(title, author, views, date, id)));
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return list;
		}
	}
	
	// Extract offset date from the string
	public static String getDate(String str) {
		// Get the units
		String timeText = "";
		int indexAgo = str.indexOf(" ago");
		int indexViews = str.indexOf("views");
		// Cut off time ago
		if (indexAgo < indexViews) {
			timeText = str.substring(0, indexAgo);
		} else {
			timeText = str.substring(indexViews);
		}
		timeText = timeText.replaceAll("[^0-9]", ""); //Only numbers
		long units = Long.parseLong(timeText);
		
		// Subtract offset from current date
		LocalDateTime ldt = LocalDateTime.now();
		if (str.contains("second")) {
			ldt = ldt.minusSeconds(units);
		} else if (str.contains("minute")) {
			ldt = ldt.minusMinutes(units);
		} else if (str.contains("hour")) {
			ldt = ldt.minusHours(units);
		} else if (str.contains("day")) {
			ldt = ldt.minusDays(units);
		} else if (str.contains("month")) {
			ldt = ldt.minusMonths(units);
		} else if (str.contains("year")) {
			ldt = ldt.minusYears(units);
		}
		
		// Return the basic date version
		ZoneId zoneId = ZoneId.systemDefault();
		String year = ""+ldt.atZone(zoneId).getYear();
		String month = ""+ldt.atZone(zoneId).getMonthValue();
		String day = ""+ldt.atZone(zoneId).getDayOfMonth();
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		return (year + month + day);
	}
	
	// Extract view count from the string
	public static int getViews(String str) {
		String viewText = "";
		int indexAgo = str.indexOf("ago");
		int indexViews = str.indexOf("views");
		// Cut off the views
		if (indexViews < indexAgo) {
			viewText = str.substring(0, indexViews);
		} else {
			viewText = str.substring(indexAgo);
		}
		viewText = viewText.replaceAll("[^0-9]", ""); // Only numbers
		return Integer.parseInt(viewText);
	}
	
	// Unique hash for video id using title + author
	public static long getId(String title, String author) {
		long hash = author.charAt(0) + title.charAt(0);
		for (int i = 0; i < title.length(); i++) {
			for (int j = 0; j < author.length(); j++) {
				hash = hash + author.charAt(j) * title.charAt(i);
			}
		}
		return hash;
	}

	public static void main(String[] args) {
		ArrayList<String> jsons = getTrendingData("https://www.youtube.com/feed/trending");
		writeListToFile(jsons, "trendingvideos.json");
	}
}
