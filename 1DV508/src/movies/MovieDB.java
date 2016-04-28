package movies;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import resources.MySQLConnection; // MySQLConnection.java


@SuppressWarnings("serial")
@Named
@SessionScoped
public class MovieDB implements Serializable{
	//	MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();
	
	private Movie temp = new Movie();
	
	public Movie getTemp() {
		return temp;
	}
	public void setTemp(Movie temp) {
		this.temp = temp;
	}
	
	/**
	 * Returns a list with all available movies
	 * 
	 * @return a list with all available movies
	 */
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
	 */
	public String add() {

		try {
			//	SQL query that adds a movie to the database.
			PreparedStatement stat = mysql.conn().prepareStatement("INSERT INTO web_shopdb.movies (title, genre, description, image_path, quantity, price) VALUES (?, ?, ?, ?, ?, ?)");
			try {
				stat.setString(1, temp.getTitle());
				stat.setString(2, temp.getGenre());
				stat.setString(3, temp.getDescription());
				stat.setString(4, temp.getImgPath());
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
	
	public String save(){
		try {
			//	SQL query that updates one movie to the database by id.
			PreparedStatement stat = mysql.conn().prepareStatement("UPDATE web_shopdb.movies SET title = ?, genre = ?, description = ?, image_path = ?, quantity = ?, price = ?  WHERE id = ?");
			try {
				stat.setString(1, temp.getTitle());
				stat.setString(2, temp.getGenre());
				stat.setString(3, temp.getDescription());
				stat.setString(4, temp.getImgPath());
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
	
	public String delete(Movie x) {
		this.temp = x;

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
}
