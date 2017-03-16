package esmaster.parser;

public class Video {
	private String title;
	private String author;
	private int views;
	private String date;
	private long id;
	private String description;
	private String category;
	
	public Video(String t, String a, int v, String da, long id, String cat, String des) {
		 setTitle(t);
		 setAuthor(a);
		 setViews(v);
		 setDate(da);
		 setId(id);
		 setCategory(cat);
		 setDescription(des);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
