package users;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Named
@SessionScoped
public class UserData implements Serializable {
	private static final String connection_url = "jdbc:mysql://localhost:3306/web_shopdb";

	public List<User> getCharacters() {
		List<User> result = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Properties user = new Properties();
			user.put("user", "group1");
			user.put("password", "UltrabookGroup1!");

			Connection conn = DriverManager.getConnection(connection_url, user);
			
			try {
				String sql = "select * from web_shopdb.users";
				
				PreparedStatement stat = conn.prepareStatement(sql);
				
				ResultSet rs = null;
				
				stat.execute();
				rs = stat.getResultSet();

				while(rs.next()) {
					User chr = new User();
					chr.setUsername(rs.getString(2));
					chr.setPassword(rs.getString(3));
					result.add(chr);
				}
			}
			finally {
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
}
