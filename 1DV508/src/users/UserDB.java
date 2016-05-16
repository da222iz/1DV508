package users;

import java.io.Serializable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;

import resources.MySQLConnection; // MySQLConnection.java

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class UserDB implements Serializable {
	//	MySQL Connection
	private MySQLConnection mysql = new MySQLConnection();
	
	private String message="";
	private User temp=new User();
	private User loginUser=new User();
	
	
	
	public String add(){
		
		try {
			//	SQL query that adds a user to the database.
			PreparedStatement stat = mysql.conn().prepareStatement("INSERT INTO users (Username, Password) VALUES (?, ?)");
			try {
				stat.setString(1, temp.getUsername());
				stat.setString(2, temp.getPassword());
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
		
		return "manage_accounts";
	}
	
	public String delete(User x){
		
		temp=x;
		
		try {
			//	SQL query to delete a user from the database by id.
			PreparedStatement stat = mysql.conn().prepareStatement("DELETE FROM users WHERE id = ?");
			//	SQL query to modify columns in an existing table.
			PreparedStatement stat1 = mysql.conn().prepareStatement("ALTER TABLE users AUTO_INCREMENT = ?");
			try {
				stat.setInt(1, temp.getId());
				stat.executeUpdate();
				
				List<User> result=getUsers();
				int increment=result.get(result.size()-1).getId()+1;
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
		
		return "manage_accounts";
		
	}
	
	public String edit(User x){
		temp=x;
		return "edit_account";
	}
	
	public String save(){
		try {
			//	SQL query that updates one user to the database by id.
			PreparedStatement stat = mysql.conn().prepareStatement("UPDATE users SET Password = ? WHERE id = ?");
			try {
				stat.setString(1, temp.getPassword());
				stat.setInt(2, temp.getId());
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
			{
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", loginUser.getUsername());
				return "admin_home.xhtml";
			}
				
							}
		this.message="Invalid username or password";
		return "index";
	}

	public List<User> getUsers() {

		List<User> result = new ArrayList<>();

		try {
			//	SQL query that retrieves all users from database.
			PreparedStatement stat = mysql.conn().prepareStatement("select * from web_shopdb.users");
			try {
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
	
	public String logout()
	{
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login";
	}
}