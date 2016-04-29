package orders;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import genres.Genre;
import movies.Movie;
import resources.MySQLConnection;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class OrderDB implements Serializable {
	//	MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();

	private Order temp = new Order();

	public Order getTemp() {
		return temp;
	}

	public void setTemp(Order temp) {
		this.temp = temp;
	}
	
	
 public List <String> statusList(){
		List<String> list = new ArrayList<>();
		list.add("New Item");
		list.add("Waiting");
		list.add("Shiped");
		
		return list;	
		
		
	}
 
 public void updateStatus(Order ord) {
	 
this.temp=ord;
		try {
			//	SQL query that adds a movie to the database.
			PreparedStatement stat = mysql.conn().prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
			try {
				stat.setString(2, temp.getStatus());
				stat.setInt(1, temp.getId());
				
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

		
	}

	public List<Order> getOrder() {
		List<Order> result = new ArrayList<>();

		try {
			//	SQL query that retrieves all movies from database.
			PreparedStatement stat = mysql.conn().prepareStatement("SELECT * FROM web_shopdb.orders");

			try {
				stat.execute();
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					Order m = new Order();
					m.setOrderNumber(rs.getInt(4));
					m.setStatus(rs.getString(2));
					m.setQuantity(rs.getInt(3));
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
}
