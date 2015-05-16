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
	
	public boolean hasLoop(String from, String to){
		Statement s;
		boolean res=false;
		try{
			s = con.createStatement();
			s.executeQuery ("SELECT * FROM forwardTable "
	       			+ "WHERE departure='"+ to +"' AND destination = '"+from+"'");
			ResultSet rs = s.getResultSet ();
			if (rs.next ()){ 
				res= true;    	
			}		
			else res= false;
			rs.close ();
			s.close ();
		} catch (SQLException exc) {
			System.out.println ("Cannot check for loops");
		}
		return res;
	}
	
	public boolean forward(String from, String to){
		Statement s;
		try{
			s = con.createStatement();
			if (!hasLoop(from,to)){
				s.executeUpdate(" INSERT INTO forwardTable (departure,destination)" +
								" VALUES " +
								" ('" + from + "', '" + to + "')"
								);
				return true;
			}
		}
		catch (SQLException exc) {
			System.out.println ("Cannot insert block");
		}
		return false;
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
			s.executeUpdate ("DROP TABLE IF EXISTS forwardTable");
			s.executeUpdate (
					"CREATE TABLE forwardTable ("
		           	+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
		           	+ "PRIMARY KEY (id),"
		           	+ "departure CHAR(40), destination CHAR(40))");
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
			System.out.println("Cannot disconnect from database server");
		}
	}
	
	public String forwardUser(String to) {
		Statement s;
		String newTo = null;
		try {
			s = con.createStatement();
			s.executeQuery ("SELECT destination FROM forwardTable "
			       			+ "WHERE departure='"+ to +"'");	
			ResultSet rs = s.getResultSet ();
			if (rs.next ())
			{
			   newTo = rs.getString ("destination");
			   //System.out.println ("forwardedTo = " + forwardedTo);
			}
			else return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newTo;
	}
	
	
	public boolean isBlocked(String blocker,String blocked) {
		Statement s,st;
		boolean res = false;
		try {
			s = con.createStatement();
			st = con.createStatement();

			s.executeQuery ("SELECT * FROM blockTable "
           			+ "WHERE blocker='"+ blocker +"' AND blocked = '"+blocked+"'");
			st.executeQuery ("SELECT * FROM blockTable "
           			+ "WHERE blocker='"+ blocked +"' AND blocked = '"+blocker+"'");
			ResultSet rs = s.getResultSet ();
			ResultSet rst = st.getResultSet ();
			if (rs.next () || rst.next()){ 
				res= true;    	
			}		
			else res= false;
			rs.close ();
			s.close ();
		}
		catch (SQLException exc) {
			System.out.println ("Cannot check block");
		}
		return res;
	}
}
