package movies;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
	
	private Movie temp = new Movie();
	private UploadedFile image;
	private String filename;
	private String searchInput="";
	private List<Movie> searchResult = this.getMovies();
	
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
		return "movie_details";
	}
	public String displayAllMovies(){
		
		this.searchResult = this.getMovies();
		
		return "user_home";
	}
	public String displayGenreMovies(Genre thegenre){
		
		this.searchResult = this.getGenreMovies(thegenre);
		
		return "user_home";
	}
	public String displaySearchResults(){
		
		this.searchResult = this.getSearchInMovies();
		
		return "user_home";
	}
	
	/**
	 * Returns a list with all available movies
	 * 
	 * @return a list with all available movies
	 */
	public List<Movie> getSearchInMovies(){
		List<Movie> result = new ArrayList<>();
		
		try {
			//	 database.
			PreparedStatement stat = mysql.conn().prepareStatement(" SELECT * FROM web_shopdb.movies WHERE title LIKE ? OR description LIKE ? ");

			try {
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
				stat.close();
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

			PreparedStatement stat = mysql.conn().prepareStatement(" SELECT * FROM web_shopdb.movies WHERE genre = ? ");

			try {
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
				stat.close();
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
			//	SQL query that retrieves all movies from database.
			PreparedStatement stat = mysql.conn().prepareStatement("SELECT * FROM web_shopdb.movies");

			try {
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
				stat.close();
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
	
	/**
	 * Adds a new movie to the database
	 * 
	 * @return return page url
	 * @throws IOException 
	 */
	public String add() throws IOException {
		//	Image upload to the local resources/images/	
		uploadImage();
		
		try {
			//	SQL query that adds a movie to the database.
			PreparedStatement stat = mysql.conn().prepareStatement("INSERT INTO web_shopdb.movies (title, genre, description, image_path, quantity, price) VALUES (?, ?, ?, ?, ?, ?)");
			try {
				stat.setString(1, temp.getTitle());
				stat.setString(2, temp.getGenre());
				stat.setString(3, temp.getDescription());
				stat.setString(4, "images/" + filename);
				stat.setInt(5, temp.getQuantity());
				stat.setFloat(6, temp.getPrice());
				stat.executeUpdate();

			} finally {
				//	Close SQL connection.
				stat.close();
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
		return "edit_movie";
	}
	
	public String save() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		//	The object will find the path to the film's poster.
		SelectImage image = new SelectImage(this.temp.getId());
		
		//	When the path to the image is treated as a file, then you can delete it from the server.
		File file = new File(image.getImgPath());	
		file.delete();
		
		//	Image upload to the local resources/images/	
		uploadImage();
	        
		try {
			//	SQL query that updates one movie to the database by id.
			PreparedStatement stat = mysql.conn().prepareStatement("UPDATE web_shopdb.movies SET title = ?, genre = ?, description = ?, image_path = ?, quantity = ?, price = ?  WHERE id = ?");
			try {
				stat.setString(1, temp.getTitle());
				stat.setString(2, temp.getGenre());
				stat.setString(3, temp.getDescription());
				stat.setString(4, "images/" + filename);
				stat.setInt(5, temp.getQuantity());
				stat.setFloat(6, temp.getPrice());
				stat.setInt(7, temp.getId());
				stat.executeUpdate();
				
			} finally {
				//	Close SQL connection.
				stat.close();
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
		SelectImage image = new SelectImage(this.temp.getId());
		
		//	When the path to the image is treated as a file, then you can delete it from the server.
		File file = new File(image.getImgPath());	
		file.delete();

		try {
			//	SQL query to delete a movie from the database by id.
			PreparedStatement stat = mysql.conn().prepareStatement("DELETE FROM web_shopdb.movies WHERE id = ?");
			//	SQL query to modify columns in an existing table.
			PreparedStatement stat1 = mysql.conn().prepareStatement("ALTER TABLE web_shopdb.movies AUTO_INCREMENT = ?");
			try {
				stat.setInt(1, temp.getId());
				stat.executeUpdate();
				
				List<Movie> result = getMovies();
				int increment = result.get(result.size() - 1).getId() + 1;
				stat1.setInt(1, increment);
				stat1.executeUpdate();

			} finally {
				//	Closes SQL connections.
				stat.close();
				stat1.close();
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
	public void uploadImage() throws IOException {
		//	Link to the local repository
		String filePath=System.getProperty("user.home") + "/git/1DV508/1DV508/WebContent/resources/images/";
				
		//	Image upload to the local resources/images/			
        if (null!=image) {
        	byte[] bytes = image.getContents();
            filename = FilenameUtils.getName(image.getFileName());
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+filename)));
            stream.write(bytes);
            stream.close();
        }
	}
	
	/**
	 * Class retrieves image from database by id.
	 */
	private class SelectImage {
		private PreparedStatement stat;
		private ResultSet rs;
		private String imgPath;
		
		/**
		 * @param id
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @throws ClassNotFoundException
		 * @throws SQLException
		 */
		public SelectImage(int id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
			this.stat = mysql.conn().prepareStatement("SELECT * FROM web_shopdb.movies WHERE id = '"+id+"'");
			stat.execute();
			
			rs = stat.getResultSet();
			
			while (rs.next()) {
				imgPath = rs.getString(5);
			}
		}
		
		/**
		 * @return path
		 */
		public String getImgPath() {
			return System.getProperty("user.home") + "/git/1DV508/1DV508/WebContent/resources/" + imgPath;
		}
	}
}
