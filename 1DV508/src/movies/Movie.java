package movies;


public class Movie {
	
	private int id;
	private String title;
	private String genre;
	private int genreId;
	private String description;
	private String imgPath;
	private int quantity;
	private float price;
	private String availabilityMessage="";
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;

	}
	public int getGenreId()
	{
		return genreId;
	}
	
	public void setGenreId(int id)
	{
		this.genreId = id;
	}
	public String getAvailabilityMessage() {
		return availabilityMessage;
	}
	public void setAvailabilityMessage(String availabilityMessage) {
		this.availabilityMessage = availabilityMessage;
	}

}
