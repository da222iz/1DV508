package resources;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import movies.Movie;
import orders.Order;

@Named
@SessionScoped
public class Cart implements Serializable {
	private MySQLConnection mysql = new MySQLConnection();
	List<Movie> contents = new ArrayList<>();

	private Order temp = new Order();

	public Order getTemp() {
		return temp;
	}

	public void setTemp(Order temp) {
		this.temp = temp;
	}

	public List<Movie> getContents() {
		return contents;
	}

	public void setContents(List<Movie> contents) {
		this.contents = contents;
	}

	public String placeOrder() {
		try {
			// SQL query that adds a movie to the database.
			PreparedStatement stat = mysql.conn().prepareStatement(
					"INSERT INTO web_shopdb.orders (status, name, address, zip, city, phone, email) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
			try {
				stat.setString(2, "NEW");
				stat.setString(3, temp.getName());
				stat.setString(4, temp.getAddress());
				stat.setString(5, temp.getZip());
				stat.setString(6, temp.getCityName());
				stat.setString(7, temp.getPhone());
				stat.setString(8, temp.getEmail());
				
				
				stat.executeUpdate();

			} finally {
				// Close SQL connection.
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

		return "";
	}

	public void AddToCart(Movie m) {
		Movie tempMovie = new Movie();
		tempMovie.setId(m.getId());
		tempMovie.setTitle(m.getTitle());
		tempMovie.setDescription(m.getDescription());
		tempMovie.setGenre(m.getGenre());
		tempMovie.setPrice(m.getPrice());
		tempMovie.setQuantity(1);
		if (this.contains(tempMovie)) {
			int index = this.getIndexOfMovie(tempMovie);
			int currentQuantity = contents.get(index).getQuantity();
			contents.get(index).setQuantity(currentQuantity + 1);
		} else {
			contents.add(tempMovie);
		}

	}

	public String emptyCart() {

		contents.clear();
		return "my_cart";
	}

	public String delete(Movie x) {
		int index = this.getIndexOfMovie(x);
		if (index > -1)
			contents.remove(index);
		return "my_cart.xhtml";
	}

	public boolean contains(Movie m) {
		boolean result = false;
		for (int i = 0; i < contents.size(); i++) {
			if (m.getTitle().equals(contents.get(i).getTitle())) {
				result = true;
				break;
			}
		}
		return result;
	}

	public int getIndexOfMovie(Movie m) {
		int index = -1;
		for (int i = 0; i < contents.size(); i++) {
			if (m.getTitle().equals(contents.get(i).getTitle())) {
				index = i;
				break;
			}
		}
		return index;
	}

}
