package resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @name MySQLConnection
 *
 */
public class MySQLConnection {
	private final static String mysql = "jdbc:mysql://localhost:3306/web_shopdb";

	/**
	 * @name getMySQL
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException
	 * @return Connection
	 * 
	 */
	public Connection conn() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		Properties user = new Properties();
		user.put("user", "group1");
		user.put("password", "UltrabookGroup1!");
		
		return DriverManager.getConnection(mysql, user);
	}
}