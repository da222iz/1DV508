package movies;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

import genres.Genre;
import resources.MySQLConnection; // MySQLConnection.java


@SuppressWarnings("serial")
@Named
@SessionScoped
public class MovieDB implements Serializable{
	//	MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();
	
	private Movie temp=new Movie();
	private UploadedFile image;
	private String searchInput="";
	private List<Movie> searchResult = this.getRandomMovies();
	private String displayMessage="Welcome to MyMovieWepShop!";
	
	// Getters and Setters for Movie
	public Movie getTemp() {
		return temp;
	}
	public void setTemp(Movie temp) {
		this.temp = temp;
	}
    
	// Getters and Setters for image
    public UploadedFile getImage() {
		return image;
	}
    
	public void setImage(UploadedFile image) {
		this.image = image;
	}
	
	
	public String displayMovieDetails(Movie m){
		temp=m;
		temp.setAvailabilityMessage("");
		return "movie_details";
	}
	public String displayAllMovies(){
		
		this.displayMessage = "You are browsing all movies";
		
		this.searchResult = this.getMovies();
		
		if (this.searchResult.size()==0){
			this.displayMessage = "No Available Movies";
		}
		
		return "index";
	}
	public String displayRandomMovies(){
		this.displayMessage = "Welcome to MyMovieWepShop!";
		
		this.searchResult = this.getRandomMovies();
		
		if (this.searchResult.size()==0){
			this.displayMessage = "No Available Movies";
		}
		
		return "index";
	}
	public String displayGenreMovies(Genre thegenre){
		
		this.displayMessage = "You are browsing "+thegenre.getValue()+" movies";
		
		this.searchResult = this.getGenreMovies(thegenre);
		
		if (this.searchResult.size()==0){
			this.displayMessage = "No Available Movies";
		}
		
		return "index";
	}
	public String displaySearchResults(){
		
		this.displayMessage = "Search results for \""+this.searchInput+" \"";
		
		this.searchResult = this.getSearchInMovies();
		
		if (this.searchResult.size()==0){
			this.displayMessage = "No Available Movies";
		}
		
		return "index";
	}
	
	/**
	 * Returns a list with all available movies
	 * 
	 * @return a list with all available movies
	 */
	public List<Movie> getSearchInMovies(){
		List<Movie> result = new ArrayList<>();
		
		try {
			Connection conn = mysql.conn();

			try {
				//	 search in movies using search option
				PreparedStatement stat = conn.prepareStatement(" SELECT * FROM web_shopdb.movies WHERE title LIKE ? OR description LIKE ? ");
				stat.setString(1, "%"+this.getSearchInput()+"%");
				stat.setString(2, "%"+this.getSearchInput()+"%");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setId(rs.getInt(1));
					m.setTitle(rs.getString(2));
					m.setGenre(rs.getString(3));
					m.setDescription(rs.getString(4));
					m.setImgPath(rs.getString(5));
					m.setQuantity(rs.getInt(6));
					m.setPrice(rs.getFloat(7));
					result.add(m);
				}

			} finally {
				//	Close SQL connection.
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public List<Movie> getRandomMovies(){
		List<Movie> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();
			
			try {
				PreparedStatement stat = conn.prepareStatement("select * from web_shopdb.movies order by rand() limit 8");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setId(rs.getInt(1));
					m.setTitle(rs.getString(2));
					m.setGenre(rs.getString(3));
					m.setDescription(rs.getString(4));
					m.setImgPath(rs.getString(5));
					m.setQuantity(rs.getInt(6));
					m.setPrice(rs.getFloat(7));
					result.add(m);
				}

			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public List<Movie> getGenreMovies(Genre genre){
		List<Movie> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();
			
			try {
				PreparedStatement stat = conn.prepareStatement(" SELECT * FROM web_shopdb.productview WHERE genre = ? ");
				stat.setString(1, genre.getValue());
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setId(rs.getInt(1));
					m.setTitle(rs.getString(2));
					m.setGenre(rs.getString(3));
					m.setDescription(rs.getString(4));
					m.setImgPath(rs.getString(5));
					m.setQuantity(rs.getInt(6));
					m.setPrice(rs.getFloat(7));
					result.add(m);
				}

			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public List<Movie> getMovies() {
		List<Movie> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();
			
			try {
				//	SQL query that retrieves all movies from database.
				PreparedStatement stat = conn.prepareStatement("SELECT * FROM web_shopdb.productview order by id");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setId(rs.getInt(1));
					m.setTitle(rs.getString(2));
					m.setGenre(rs.getString(3));
					m.setDescription(rs.getString(4));
					m.setImgPath(rs.getString(5));
					m.setQuantity(rs.getInt(6));
					m.setPrice(rs.getFloat(7));
					result.add(m);
				}

			} finally {
				//	Close SQL connection.
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String goToAdd(){
		this.temp = new Movie();
		this.setImage(null);
		return "add_movie";
	}
	
	/**
	 * Adds a new movie to the database
	 * 
	 * @return return page url
	 * @throws IOException 
	 */
	public String add(){
		
		//	Image upload to the local resources/images/	
		uploadImage();
		if (this.temp.getImgPath()==null){
			this.temp.setImgPath("images/not_available.jpg");
		}
		
		try {
			Connection conn = mysql.conn();

			try {
				//	SQL query that adds a movie to the database.
				String query="INSERT INTO web_shopdb.movies (title, genre, description, image_path, quantity, price) VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement stat = conn.prepareStatement(query);
				stat.setString(1, temp.getTitle());
				stat.setInt(2, temp.getGenreId());
				stat.setString(3, temp.getDescription());
				stat.setString(4, temp.getImgPath());
				stat.setInt(5, temp.getQuantity());
				stat.setFloat(6, temp.getPrice());
				stat.executeUpdate();

			} finally {
				//	Close SQL connection.
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return "manage_movies";
	}
	
	
	public String edit(Movie m) {
		this.temp = m;
		this.setImage(null);
		return "edit_movie";
	}
	
	public List<Movie> getImageDuplicates(String path){
		List<Movie> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();

			try {
				PreparedStatement stat = conn.prepareStatement(" SELECT * FROM web_shopdb.movies WHERE image_path = ? ");
				stat.setString(1, path);
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setId(rs.getInt(1));
					m.setTitle(rs.getString(2));
					m.setGenre(rs.getString(3));
					m.setDescription(rs.getString(4));
					m.setImgPath(rs.getString(5));
					m.setQuantity(rs.getInt(6));
					m.setPrice(rs.getFloat(7));
					result.add(m);
				}

			} finally {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	public String save(){
		if(!(this.image.getFileName().equals(""))){
			//	The object will find the path to the film's poster.
			String tempPath = System.getProperty("user.home") + "/git/1DV508/1DV508/WebContent/resources/" +this.temp.getImgPath();
			
			List<Movie> imageDuplicates=this.getImageDuplicates(this.temp.getImgPath());
			
			//	When the path to the image is treated as a file, then you can delete it from the server.
			if(imageDuplicates.size()<=1 && !(this.temp.getImgPath().equals("images/not_available.jpg"))){
				File file = new File(tempPath);	
				file.delete();
			}
			
			//	Image upload to the local resources/images/	
			uploadImage();
		}
	        
		try {
			Connection conn = mysql.conn();

			try {
				//	SQL query that updates one movie to the database by id.
				String query="UPDATE web_shopdb.movies SET title = ?, genre = ?, description = ?, image_path = ?, quantity = ?, price = ?  WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(query);
				stat.setString(1, temp.getTitle());
				stat.setInt(2, temp.getGenreId());
				stat.setString(3, temp.getDescription());
				stat.setString(4, temp.getImgPath());
				stat.setInt(5, temp.getQuantity());
				stat.setFloat(6, temp.getPrice());
				stat.setInt(7, temp.getId());
				stat.executeUpdate();
				
			} finally {
				//	Close SQL connection.
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return "manage_movies";
		
	}
	
	public String delete(Movie x) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.temp = x;
		
		//	The object will find the path to the film's poster.
		String tempPath = System.getProperty("user.home")+"/git/1DV508/1DV508/WebContent/resources/"+this.temp.getImgPath();
		System.out.println(tempPath);
		
		List<Movie> imageDuplicates=this.getImageDuplicates(this.temp.getImgPath());
		
		//	When the path to the image is treated as a file, then you can delete it from the server.
		if(imageDuplicates.size()<=1 && !(this.temp.getImgPath().equals("images/not_available.jpg"))){
			File file = new File(tempPath);	
			file.delete();
		}

		try {
			Connection conn = mysql.conn();

			try {
				//	SQL query to delete a movie from the database by id.
				PreparedStatement stat = conn.prepareStatement("DELETE FROM web_shopdb.movies WHERE id = ?");
				stat.setInt(1, temp.getId());
				stat.executeUpdate();
				
				//	SQL query to modify columns in an existing table.
				PreparedStatement stat1 = conn.prepareStatement("ALTER TABLE web_shopdb.movies AUTO_INCREMENT = ?");
				List<Movie> result = getMovies();
				int increment = result.get(result.size() - 1).getId() + 1;
				stat1.setInt(1, increment);
				stat1.executeUpdate();

			} finally {
				//	Closes SQL connections.
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return "manage_movies";
	}
	
	public String getSearchInput() {
		return searchInput;
	}
	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}
	public List<Movie> getSearchResult() {
		return searchResult;
	}
	public void setSearchResult(List<Movie> searchResult) {
		this.searchResult = searchResult;
	}
	
	
	/**
	 * This java method will upload the image file to the local repository.
	 * 
	 * @throws IOException
	 */
	public void uploadImage(){
		try{
			//	Link to the local repository
			String filePath=System.getProperty("user.home") + "/git/1DV508/1DV508/WebContent/resources/images/";
					
			//	Image upload to the local resources/images/			
	        if (!(this.image.getFileName().equals(""))) {
	        	byte[] bytes = image.getContents();
	            String filename = FilenameUtils.getName(image.getFileName());
	            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+filename)));
	            stream.write(bytes);
	            stream.close();
	            this.getTemp().setImgPath("images/" + filename);
	        }
			
		}
		catch(Exception e){	
		}
	}
	public String getDisplayMessage() {
		return displayMessage;
	}
	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}	
	
}