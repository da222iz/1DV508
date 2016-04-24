package users;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named
@SessionScoped
public class UserDB implements Serializable {
	
	private static final String connection_url = "jdbc:mysql://localhost:3306/web_shopdb";
	private String message="";
	private User temp=new User();
	private User loginUser=new User();
	
	
	
	public String add(){
		
		try {
			Connection conn = newConnection();
			try {
				String sql = "INSERT INTO users (Username, Password) VALUES (?, ?)";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setString(1, temp.getUsername());
				stat.setString(2, temp.getPassword());
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
		
		return "manage_accounts";
	}
	
	public String delete(User x){
		
		temp=x;
		
		try {
			Connection conn = newConnection();
			try {
				String sql = "DELETE FROM users WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setInt(1, temp.getId());
				stat.executeUpdate();
				
				String sql1 = "ALTER TABLE users AUTO_INCREMENT = ?";
				PreparedStatement stat1 = conn.prepareStatement(sql1);
				List<User> result=getUsers();
				int increment=result.get(result.size()-1).getId()+1;
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
		
		return "manage_accounts";
		
	}
	
	public String edit(User x){
		temp=x;
		return "edit_account";
	}
	
	public String save(){
		try {
			Connection conn = newConnection();
			try {
				String sql = "UPDATE users SET Username = ?, Password = ? WHERE id = ?";
				PreparedStatement stat = conn.prepareStatement(sql);
				stat.setString(1, temp.getUsername());
				stat.setString(2, temp.getPassword());
				stat.setInt(3, temp.getId());
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
		
		return "manage_accounts";
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage() {
		this.message = checkUser();
	}
	
	public String checkUser(){
	
		List<User> result=getUsers();
		
		
		for (int i = 0; i < result.size(); i++) {
			if (loginUser.getUsername().equals(result.get(i).getUsername()) && loginUser.getPassword().equals(result.get(i).getPassword()))
				
				return "login";
							}
		this.message="Invalid username or password";
		return "index";
	}

	public List<User> getUsers() {

		List<User> result = new ArrayList<>();

		try {
			Connection conn = newConnection();
			try {
				String sql = "select * from web_shopdb.users";
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
				while (rs.next()) {
					User u = new User();
					u.setId(rs.getInt(1));
					u.setUsername(rs.getString(2));
					u.setPassword(rs.getString(3));
					result.add(u);
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

	public User getTemp() {
		return temp;
	}

	public void setTemp(User thetemp) {
		this.temp = thetemp;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User theloginUser) {
		this.loginUser = theloginUser;
	}
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