package resources;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	List<CartContents> cart = new ArrayList<>();
	
	
	public List<CartContents> getCart() {
		return cart;
	}

	public void setCart(List<CartContents> cart) {
		this.cart = cart;
	}
	
	private float totalPrice = 0;
	
	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

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
	
	private boolean orderNumberExists(int n){
		boolean exists=false;
		List<Order> allOrders = new ArrayList<>();

		try {
			Connection conn = mysql.conn();

			try {
				//	SQL query that retrieves all movies from database.
				PreparedStatement stat = conn.prepareStatement("SELECT * FROM web_shopdb.orders");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Order m = new Order();
					m.setOrderNumber(rs.getInt(9));
					m.setStatus(rs.getString(2));
					m.setName(rs.getString(3));
					allOrders.add(m);
					
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
		for (int i=0; i<allOrders.size(); i++){
			if(allOrders.get(i).getOrderNumber()==n){
				exists=true;
				break;
			}
		}
		return exists;
		
	}

	public String placeOrder() {
		if (cart.size()!=0){
			do{
				random = (int) Math.floor((Math.random() * 1000000000)+10000);
			}while(this.orderNumberExists(random));
			
			//random = (int) System.nanoTime();
			try {
				Connection conn = mysql.conn();
				

				try {
					PreparedStatement stat = conn.prepareStatement("INSERT INTO web_shopdb.orders ( status, name, address, zip, city, phone, email, order_number, total_price) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					PreparedStatement stat1 = conn.prepareStatement("UPDATE web_shopdb.movies SET quantity = ? WHERE id = ?");
					PreparedStatement stat2 = conn.prepareStatement("INSERT INTO web_shopdb.ordered_movies ( order_number, movie_title, quantity) VALUES ( ?, ?, ?)");

					stat.setString(1, "NEW");
					stat.setString(2, temp.getName());
					stat.setString(3, temp.getAddress());
					stat.setInt(4, temp.getZip());
					stat.setString(5, temp.getCityName());
					stat.setInt(6, temp.getPhone());
					stat.setString(7, temp.getEmail());
					stat.setInt(8, random);
					stat.setFloat(9, getTotalPrice());
					
					for (int i = 0; i < cart.size(); i++) {
						stat2.setInt(1, random);
						stat2.setString(2, cart.get(i).getMovie().getTitle());
						stat2.setInt(3, cart.get(i).getNumber());
						stat1.setInt(1, (cart.get(i).getMovie().getQuantity() - cart.get(i).getNumber()));
						stat1.setInt(2, cart.get(i).getMovie().getId());
						
						stat2.executeUpdate();
						stat1.executeUpdate();
					}
					
					stat.executeUpdate();

				} finally {
					// Close SQL connection.
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

			emptyCart();
			return "order_info";
			
		}
		return "user_info";


	}

	/*public void AddToCart(Movie m) {
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

	}*/
	
	/**
	 * Add a movie to Cart
	 * @param m
	 */
	public void AddToCart(Movie m) {
		/*float totalPrice;
		if (contains(m) == true) {
			CartContents current = this.getIndexOfMovie(m);
			if (current.getNumber() < current.getMovie().getQuantity())) {
				current.setNumber(current.getNumber() + 1);
				totalPrice = (this.getTotalPrice() - (current.getMovie().getPrice() * (current.getNumber() - 1))) + (current.getMovie().getPrice() * current.getNumber());
				setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
			}
		} else {
			cart.add(new CartContents(m,1));
			totalPrice = this.getTotalPrice() + m.getPrice();
			setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
		}*/	
		
		if (contains(m) == false) {
			if (m.getQuantity() > 0) {
				cart.add(new CartContents(m,1));
				
				float totalPrice = this.getTotalPrice() + m.getPrice();
				setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
			}
			else{
				m.setAvailabilityMessage("Movie out of stock!");
			}
		}
		else{
			m.setAvailabilityMessage("Movie already in the Cart!");
		}
	}

	/**
	 * decrease the number by 1
	 * @param c
	 * @return
	 */
	public String decreaseQuantity(CartContents c) {
		//	can not order fewer than 1.
		if (c.getNumber() > 1) {
			c.setNumber(c.getNumber() - 1);
			float totalPrice = this.getTotalPrice() - (c.getMovie().getPrice());
			setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
		}
		return "my_cart";
	}

	/**
	 * increases the number by 1
	 * @param c
	 * @return
	 */
	public String increaseQuantity(CartContents c) {
		Movie tempMovie = c.getMovie();
		//	can not order more than the quantity available in the database.
		if (c.getNumber() < tempMovie.getQuantity()) {
			c.setNumber(c.getNumber() + 1);
			float totalPrice = (this.getTotalPrice() - (c.getMovie().getPrice() * (c.getNumber() - 1))) + (c.getMovie().getPrice() * c.getNumber());
			setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
		}
		return "my_cart";
	}

	/*public String emptyCart() {

		contents.clear();
		return "my_cart";
	}*/
	
	/**
	 * Delete all objects from cart
	 * @return
	 */
	public String emptyCart() {
		cart.clear();
		this.setTotalPrice(0);
		return "my_cart";
	}

	/*public String delete(Movie x) {
		int index = this.getIndexOfMovie(x);
		if (index > -1)
			contents.remove(index);
		return "my_cart.xhtml";
	}*/
	
	/**
	 * Delete object from cart
	 * @param x
	 * @return
	 */
	public String delete(CartContents x) {
		for (int i = 0; i < cart.size(); i++) {
			if(x.equals(cart.get(i))) {
				float totalPrice = this.getTotalPrice() - (x.getMovie().getPrice() * x.getNumber());
				setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
				cart.remove(i);
			}
		}
		
		return "my_cart.xhtml";
	}

	/*public boolean contains(Movie m) {
		boolean result = false;
		for (int i = 0; i < contents.size(); i++) {
			if (m.getTitle().equals(contents.get(i).getTitle())) {
				result = true;
				break;
			}
		}
		return result;
	}*/
	
	
	/**
	 * Compare Movie objects
	 * @param m
	 * @return
	 */
	public boolean contains(Movie m) {		
		for (int i = 0; i < cart.size(); i++) {
			int id = cart.get(i).getMovie().getId();
			
			if(id == m.getId()) {
				return true;
			}
		}
		
		return false;
	}

	/*public int getIndexOfMovie(Movie m) {
		int index = -1;
		for (int i = 0; i < contents.size(); i++) {
			if (m.getTitle().equals(contents.get(i).getTitle())) {
				index = i;
				break;
			}
		}
		return index;
	}*/
	
	/**
	 * This method pick a movie out of the basket
	 * @param m
	 * @return
	 */
	public CartContents getIndexOfMovie(Movie m) {		
		for (int i = 0; i < cart.size(); i++) {
			int id = cart.get(i).getMovie().getId();
			
			if(m.getId() == id) {
				return cart.get(i);
			}
		}		
		return null;
	}
}
