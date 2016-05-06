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

@SuppressWarnings("serial")
@Named
@SessionScoped
public class Cart implements Serializable {
	private MySQLConnection mysql = new MySQLConnection();
	List<Movie> contents = new ArrayList<>();
	int random;

	private Order temp = new Order();

	public Order getTemp() {
		return temp;
	}

	public void setTemp(Order temp) {
		this.temp = temp;
	}

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public List<Movie> getContents() {
		return contents;
	}

	public void setContents(List<Movie> contents) {
		this.contents = contents;
	}

	public String placeOrder() {
		
		random= (int) Math.floor((Math.random() * 1000000000)+10000);
		try {
			// SQL query that adds a movie to the database.
			PreparedStatement stat = mysql.conn().prepareStatement(
					"INSERT INTO web_shopdb.orders ( status, name, address, zip, city, phone, email, order_number) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)");
			try {

				stat.setString(1, "NEW");
				stat.setString(2, temp.getName());
				stat.setString(3, temp.getAddress());
				stat.setInt(4, temp.getZip());
				stat.setString(5, temp.getCityName());
				stat.setInt(6, temp.getPhone());
				stat.setString(7, temp.getEmail());
				stat.setInt(8, random);
				
				
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

		emptyCart();
		return "order_info";

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

	public String decreaseQuantity(Movie m) {
		Movie tempMovie = new Movie();
		tempMovie.setId(m.getId());
		tempMovie.setTitle(m.getTitle());
		tempMovie.setDescription(m.getDescription());
		tempMovie.setGenre(m.getGenre());
		tempMovie.setPrice(m.getPrice());
		tempMovie.setQuantity(1);
		int index = this.getIndexOfMovie(tempMovie);
		int currentQuantity = contents.get(index).getQuantity();
		if (currentQuantity != 0) {
			contents.get(index).setQuantity(currentQuantity - 1);
		}
		return "my_cart";
	}

	public String increaseQuantity(Movie m) {
		Movie tempMovie = new Movie();
		tempMovie.setId(m.getId());
		tempMovie.setTitle(m.getTitle());
		tempMovie.setDescription(m.getDescription());
		tempMovie.setGenre(m.getGenre());
		tempMovie.setPrice(m.getPrice());
		tempMovie.setQuantity(1);
		int index = this.getIndexOfMovie(tempMovie);
		int currentQuantity = contents.get(index).getQuantity();
		contents.get(index).setQuantity(currentQuantity + 1);
		return "my_cart";
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
