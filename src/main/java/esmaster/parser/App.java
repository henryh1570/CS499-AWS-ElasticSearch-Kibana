package esmaster.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

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
				for (String str : data) {
					p.write(str + "\n");
				}
				p.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<String> getTrendingData(String url) {
		Gson g = new Gson();

		ArrayList<String> list = new ArrayList<String>();
		
		try {
			URL html = new URL(url);
			Document doc = Jsoup.parse(html, 10000);
			Elements feeds = doc.select(".yt-lockup-content");
			for (Element element : feeds) {
				String title = (element.select("a").attr("title")); //Video Title
				String author = (element.select(".yt-lockup-byline").text()); // Uploader
				String dateViews = (element.select(".yt-lockup-meta-info").text()); // Date + view count
				int views = getViews(dateViews);
				long date = getDate(dateViews);
				list.add(g.toJson(new Video(title, author, views, date)));
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return list;
		}
	}
	
	public static long getDate(String str) {
		// Get the units
		int indexAgo = str.indexOf(" ago");
		String timeText = str.substring(0, indexAgo).toLowerCase(); // Remove ago		
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
		
		// Return the epoch version
		ZoneId zoneId = ZoneId.systemDefault();
		long epoch = ldt.atZone(zoneId).toEpochSecond();
		return epoch;
	}
	
	public static int getViews(String str) {
		int index = str.indexOf("ago ");
		String viewText = str.substring(index + 4); // Remove days/weeks ago
		viewText = viewText.replaceAll("[^0-9]", ""); // Only numbers
		return Integer.parseInt(viewText);
	}

	public static void main(String[] args) {
		ArrayList<String> jsons = getTrendingData("https://www.youtube.com/feed/trending");

		for (String s : jsons) {
			System.out.println(s);
		}
	}
}
