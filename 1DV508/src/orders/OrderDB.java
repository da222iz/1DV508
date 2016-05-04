package orders;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import resources.MySQLConnection;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class OrderDB implements Serializable {
	//	MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();
	
	private int order_number;
	
	//	Getters and setters for order number.
	public int getOrder_number() {
		return order_number;
	}
	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}
	
	

	private List<Order> allOrders=getOrder();

	
	
	public String[]  statusArray(){
		String[] status=new String[3];
		status[0]="NEW";
		status[1]="SHIPPED";
		status[2]="DELAYED";
		
		return status;	
		
		
	}
	public int indexOfOrder(Order x){
		int id=x.getId();
		int result=-1;
		for (int i=0; i<allOrders.size(); i++){
			if (allOrders.get(i).getId()==id){
				result=i;
				break;
			}
		}
		return result;
	}
	
	public String delete(Order x) {

		try {
			//	SQL query to delete a movie from the database by id.
			PreparedStatement stat = mysql.conn().prepareStatement("DELETE FROM web_shopdb.orders WHERE order_id = ?");
			//	SQL query to modify columns in an existing table.
			PreparedStatement stat1 = mysql.conn().prepareStatement("ALTER TABLE web_shopdb.orders AUTO_INCREMENT = ?");
			try {
				stat.setInt(1, x.getId());
				stat.executeUpdate();
				
				List<Order> result = getOrder();
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

		return "manage_order";
	}
 
 	public String updateStatus() {
 			for(int i=0; i<allOrders.size(); i++){
 				
 				try {
 					//	SQL query that adds a movie to the database.
 					PreparedStatement stat = mysql.conn().prepareStatement(" UPDATE web_shopdb.orders SET status = ? WHERE order_id = ? ");
 					try {
 						stat.setString(1, allOrders.get(i).getStatus());
 						stat.setInt(2, allOrders.get(i).getId());
 						
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
		return "manage_order";
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
					m.setId(rs.getInt(1));
					m.setStatus(rs.getString(2));
					m.setName(rs.getString(3));
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

	public List<Order> getAllOrders() {
		return allOrders;
	}

	public void setAllOrders(List<Order> allOrders) {
		this.allOrders = allOrders;
	}
	
	public String getStatus() {
		String status = "No order found!";
		
		try {
			PreparedStatement stat = mysql.conn().prepareStatement("SELECT * FROM web_shopdb.orders WHERE order_number = '"+this.order_number+"'");

			try {
				stat.execute();
				
				ResultSet rs = stat.getResultSet();
				while (rs.next()) {
					status = rs.getString(2);				
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
		return status;
	}
}
