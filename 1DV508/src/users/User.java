package users;
// Adding a useless comment for testing purposes...
public class User{

	private String username;
	private String password;
	private int id;

	public int getId() {
		return id;
	}

	//	setId
	public void setId(int theId) {
		this.id = theId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String theusername) {
		this.username = theusername;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String thepassword) {
		this.password = thepassword;
	}



}
