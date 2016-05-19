package genres;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import resources.MySQLConnection; // MySQLConnection.java

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Danielsson on 2016-04-22. pd222dj@student.lnu.se
 */

@SuppressWarnings("serial")
@Named
@SessionScoped
public class GenreDB implements Serializable {
	//	MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();
	
	private Genre temp = new Genre();

	public Genre getTemp() {
		return this.temp;
	}

	public void setTemp(Genre genre) {
		this.temp = genre;
	}

	/**
	 * Returns a list with all available genres
	 * 
	 * @return a list with all available genres
	 */
	public List<Genre> getGenres() {
		List<Genre> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();
			
			try {
				//	SQL query that retrieves all genres from database.
				PreparedStatement stat = conn.prepareStatement("SELECT * FROM web_shopdb.genres");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Genre g = new Genre();
					g.setId(rs.getInt(1));
					g.setValue(rs.getString(2));
					result.add(g);
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
	/**
	 * gets the genre list into a String array containing the genres' values
	 * 
	 * needed for the dropdown menu to select genre.
	 */
	public String[] getGenresArray(){
		List<Genre> genreList = getGenres();
		String[] result= new String[genreList.size()];
		
		for (int i=0; i<result.length; i++){
			result[i]=genreList.get(i).getValue();
		}
		
		return result;
	}

	/**
	 * Adds a new genre to the database
	 * 
	 * @return return page url
	 */
	public String add() {

		try {
			Connection conn = mysql.conn();

			try {
				//	SQL query that adds a genre to the database.
				PreparedStatement stat = conn.prepareStatement("INSERT INTO web_shopdb.genres (genre) VALUES (?)");
				stat.setString(1, temp.getValue());
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

		return "manage_genres";
	}

	/**
	 * Edit a genre
	 * 
	 * @param g
	 *            Genre object
	 * @return return page url
	 */
	public String edit(Genre g) {
		this.temp = g;
		return "edit_genre";
	}

	public String save() {
		try {
			Connection conn = mysql.conn();

			try {
				//	SQL query that updates one genre to the database by id.
				PreparedStatement stat = conn.prepareStatement("UPDATE web_shopdb.genres SET genre = ? WHERE id = ?");
				stat.setString(1, temp.getValue());
				stat.setInt(2, temp.getId());
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

		return "manage_genres";
	}

	public String delete(Genre genre) {
		this.temp = genre;

		try {
			Connection conn = mysql.conn();

			try {
				
				//	SQL query to delete a genre from the database by id.
				PreparedStatement stat = conn.prepareStatement("DELETE FROM web_shopdb.genres WHERE id = ?");
				//	SQL query to modify columns in an existing table.
				PreparedStatement stat1 = conn.prepareStatement("ALTER TABLE web_shopdb.genres AUTO_INCREMENT = ?");
				stat.setInt(1, temp.getId());
				stat.executeUpdate();

				List<Genre> result = getGenres();
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

		return "manage_genres";
	}
}
