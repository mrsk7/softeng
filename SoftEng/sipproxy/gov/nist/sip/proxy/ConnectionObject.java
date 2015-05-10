package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionObject {
	
	protected Connection con;
	
	public ConnectionObject() {
		connect();
		createTables();
	}
	
	public void block(String blocker, String ebloki){
		Statement s;
		try{
			s = con.createStatement();
			s.executeUpdate(" INSERT INTO blockTable (blocker,blocked)" +
							" VALUES " +
							" ('" + blocker + "', '" + ebloki + "')"
							);
		}
		catch (SQLException exc) {
			System.out.println ("Cannot insert block");
		}
	}
	public void connect() {
		Connection con = null;
		
		try {
	        System.out.println("EDW");
	        String url = "jdbc:mysql://83.212.112.64:3306/test";
	        String user = "p";
	        String password = "1";
	        con = DriverManager.getConnection(url, user, password);
		}
		catch (Exception e) 
		{
			System.out.println ("Cannot connect to database server");
		}
		this.con = con;
	}
	
	public void createTables() {
		Statement s;
		try {
			s = con.createStatement();
			s.executeUpdate ("DROP TABLE IF EXISTS blockTable");
			s.executeUpdate (
						"CREATE TABLE blockTable ("
			           	+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
			           	+ "PRIMARY KEY (id),"
			           	+ "blocker CHAR(40), blocked CHAR(40))");
		     s.close();
		}
		catch (SQLException exc) {
			System.out.println ("Cannot create TABLE");
		}
	}
	
	public void disconnect() {
		try {
			if (con != null) {
	            con.close();
	        }
		}
		catch (SQLException ex) {
			System.out.println("Cannoct disconnect from database server");
		}
	}

}
