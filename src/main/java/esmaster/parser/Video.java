package esmaster.parser;

public class Video {
	private String title;
	private String author;
	private int views;
	private long date;
	
	public Video(String t, String a, int v, long d) {
		 setTitle(t);
		 setAuthor(a);
		 setViews(v);
		 setDate(d);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}
