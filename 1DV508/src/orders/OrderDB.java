package orders;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import movies.Movie;
import resources.MySQLConnection;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class OrderDB implements Serializable {
	// MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();

	private String order_number;

	// Getters and setters for order number.

	private Order o = new Order();
	private Order temp = new Order();
	


	private List<Order> allOrders;

	// Getters and setters for order number.
	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	// Getters and setters for Order o.
	public Order getO() {
		return o;
	}

	public void setO(Order o) {
		this.o = o;
	}
	
	public void getOrderDetails(Order order){
		this.temp = order;
	}
	public String[] statusArray() {
		String[] status = new String[3];
		status[0] = "NEW";
		status[1] = "SHIPPED";
		status[2] = "DELAYED";

		return status;

	}

	public int indexOfOrder(Order x) {
		
		int id = x.getOrderNumber();
		int result = 0;
		for (int i = 0; i < allOrders.size(); i++) {
			if (allOrders.get(i).getOrderNumber() == id) {
				result = i;
				break;
			}
		}
		return result;
	}

	public String delete(Order x) {
		if (x.getStatus().equals("SHIPPED")){

			try {
				Connection conn = mysql.conn();

				try {
					// SQL query to delete a movie from the database by id.
					PreparedStatement stat = conn.prepareStatement("DELETE FROM web_shopdb.orders WHERE order_number = ?");
					stat.setInt(1, x.getOrderNumber());
					stat.executeUpdate();
					
					PreparedStatement stat2 = conn.prepareStatement("DELETE FROM web_shopdb.ordered_movies WHERE order_number = ?");
					stat2.setInt(1, x.getOrderNumber());
					stat2.executeUpdate();

					// SQL query to modify columns in an existing table.
					PreparedStatement stat1 = conn.prepareStatement("ALTER TABLE web_shopdb.orders AUTO_INCREMENT = ?");
					List<Order> result = getOrder();
					int increment = result.get(result.size() - 1).getId() + 1;
					stat1.setInt(1, increment);
					stat1.executeUpdate();

				} finally {
					// Closes SQL connections.
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
		}
		return "manage_order";
	}

	public List<Movie> getOrderMovies(int orderNum) {
		List<Movie> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();

			try {
				PreparedStatement stat = conn
						.prepareStatement(" SELECT * FROM web_shopdb.ordered_movies WHERE order_number = ? ");
				stat.setInt(1, orderNum);
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Movie m = new Movie();
					m.setTitle(rs.getString(2));
					m.setQuantity(rs.getInt(3));
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

	public Order getLatestOrder() {
		List<Order> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();

			try {
				// SQL query that retrieves the latest order from database
				PreparedStatement stat = conn
						.prepareStatement("SELECT * FROM web_shopdb.orders ORDER BY id DESC LIMIT 1;");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Order m = new Order();
					m.setId(rs.getInt(1));
					m.setStatus(rs.getString(2));
					m.setName(rs.getString(3));
					m.setAddress(rs.getString(4));
					m.setZip(rs.getInt(5));
					m.setCityName(rs.getString(6));
					m.setPhone(rs.getLong(7));
					m.setEmail(rs.getString(8));
					m.setOrderNumber(rs.getInt(9));
					m.setTotalPrice(rs.getFloat(10));

					result.add(m);

				}

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
		return result.get(0);
	}

	public String updateStatus() {
		for (int i = 0; i < allOrders.size(); i++) {

			try {
				Connection conn = mysql.conn();

				try {
					// SQL query that updates the status of all movies
					PreparedStatement stat = conn
							.prepareStatement(" UPDATE web_shopdb.orders SET status = ? WHERE order_number = ? ");
					stat.setString(1, allOrders.get(i).getStatus());
					stat.setInt(2, allOrders.get(i).getOrderNumber());

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
		}
		return "manage_order";
	}

	public List<Order> getOrder() {
		List<Order> result = new ArrayList<>();

		try {
			Connection conn = mysql.conn();

			try {
				// SQL query that retrieves all movies from database.
				PreparedStatement stat = conn.prepareStatement("SELECT * FROM web_shopdb.orders");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Order m = new Order();
					m.setId(rs.getInt(1));
					m.setStatus(rs.getString(2));
					m.setName(rs.getString(3));
					m.setAddress(rs.getString(4));
					m.setZip(rs.getInt(5));
					m.setCityName(rs.getString(6));
					m.setPhone(rs.getLong(7));
					m.setEmail(rs.getString(8));
					m.setOrderNumber(rs.getInt(9));
					m.setTotalPrice(rs.getFloat(10));
					result.add(m);

				}

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
		this.setAllOrders(result);
		return result;
	}

	public List<Order> getAllOrders() {
		return allOrders;
	}

	public void setAllOrders(List<Order> allOrders) {
		this.allOrders = allOrders;
	}
	public boolean statusExists(String status){
		boolean exists=false;
		if(status.equals("NEW")||status.equals("SHIPPED")||status.equals("DELAYED")){
			exists=true;
		}
		return exists;
	}

	public String search() {
		this.o = new Order();
		try {
			Connection conn = mysql.conn();

			try {
				PreparedStatement stat = conn.prepareStatement(
						"SELECT * FROM web_shopdb.orders WHERE order_number = '" + this.order_number + "'");
				stat.execute();
				ResultSet rs = stat.getResultSet();
				if (rs.next()) {
					do {this.o.setTotalPrice(rs.getFloat(10));
						this.o.setOrderNumber(rs.getInt(9));
						this.o.setStatus(rs.getString(2));
					} while (rs.next());
				} else if (this.order_number.length() > 0) {
					this.o.setStatus("The order does not exist or has already been delivered.");
				} else {
					this.o.setStatus("You must fill in an order number!");
				}

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
		return "support";
	}

	public String supportLink() {
		setOrder_number("");
		this.o = new Order();
		return "support";

	}

	public Order getTemp() {
		return temp;
	}

	public void setTemp(Order temp) {
		this.temp = temp;
	}
}
