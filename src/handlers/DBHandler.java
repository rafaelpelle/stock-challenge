package handlers;
import java.sql.*;

public class DBHandler {
	private static final String JDBC_DRIVER = "com.filemaker.jdbc.Driver";
	private static final String DB_URL = "jdbc:filemaker://localhost/stock-db";
	private static final String DB_USER = "Admin";
	private static final String DB_PASS = "Admin";

	public static Connection getDBConnection() {
		try {
			Driver d = (Driver)Class.forName(JDBC_DRIVER).newInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
		Connection con = null;
		try {
			con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static void closeConnection(Connection con) {
		try {
			if(con != null) {
				con.close();
				System.out.println("Connection is closed...");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
