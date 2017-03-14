package esmaster.parser;

public class Video {
	private String title;
	private String author;
	private int views;
	private String date;
	private long id;
	
	public Video(String t, String a, int v, String d, long id) {
		 setTitle(t);
		 setAuthor(a);
		 setViews(v);
		 setDate(d);
		 setId(id);
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
