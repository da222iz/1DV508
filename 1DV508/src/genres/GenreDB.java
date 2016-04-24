package genres;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Peter Danielsson on 2016-04-22. pd222dj@student.lnu.se
 */

@Named
@SessionScoped
public class GenreDB implements Serializable {
	private static final String connection_url = "jdbc:mysql://localhost:3306/web_shopdb";
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

			Connection conn = newConnection();

			try {
				String sql = "SELECT * FROM web_shopdb.genres";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Genre g = new Genre();
					g.setId(rs.getInt(1));
					g.setValue(rs.getString(2));
					result.add(g);
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
			Connection conn = newConnection();
			try {
				String sql = "INSERT INTO web_shopdb.genres (genre) VALUES (?)";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setString(1, temp.getValue());
				stat.executeUpdate();

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
			Connection conn = newConnection();
			try {
				String sql = "UPDATE web_shopdb.genres SET genre = ? WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setString(1, temp.getValue());
				stat.setInt(2, temp.getId());
				stat.executeUpdate();

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

		return "manage_genres";
	}

	public String delete(Genre genre) {
		this.temp = genre;

		try {
			Connection conn = newConnection();
			try {
				String sql = "DELETE FROM web_shopdb.genres WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setInt(1, temp.getId());
				stat.executeUpdate();

				String sql1 = "ALTER TABLE web_shopdb.genres AUTO_INCREMENT = ?";
				PreparedStatement stat1 = conn.prepareStatement(sql1);
				List<Genre> result = getGenres();
				int increment = result.get(result.size() - 1).getId() + 1;
				stat1.setInt(1, increment);
				stat1.executeUpdate();

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

		return "manage_genres";
	}

	/**
	 * Private help method to retrieve a new connection to the database
	 * 
	 * @return Connection to database
	 */
	private Connection newConnection()
			throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Properties user = new Properties();
		user.put("user", "group1");
		user.put("password", "UltrabookGroup1!");
		Connection conn = DriverManager.getConnection(connection_url, user);

		return conn;
	}
}
