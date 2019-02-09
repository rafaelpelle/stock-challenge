package main;
import java.sql.*;
import modelo.Participante;

class ScriptCriacaoDeParticipante {
	   static final String JDBC_DRIVER = "com.filemaker.jdbc.Driver";  
	   static final String DB_URL = "jdbc:filemaker://localhost/database";
	   static final String DB_USER = "Admin";
	   static final String DB_PASS = "password";
	
	public static void main(String[ ] args) {
		Connection con = getDBConnection();
		listConnectionWarnings(con);
		int idCarteira = createWallet(con);
		Participante par = new Participante(idCarteira, "Ativo", "08194709962", "Rafael Pelle");
		int id = createUser(con, par);
		closeConnection(con);
		System.out.println("id: " + id + "\nidCarteira: " + idCarteira);
		System.out.println("Finished...");
	}
	
	private static int createWallet(Connection con) {
		System.out.println("Creating wallet...");
		String sqlQuery = "INSERT INTO Carteira (contribuicaoNormal, contribuicaoAdicional, contribuicaoPortabilidade, contribuicaoPlanoPrevComplementar, contribuicaoSociedadeSeguradora)"
				+ "VALUES (0, 0, 0, 0, 0)";
		PreparedStatement stmt = null;
		int idCarteira = -1;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	idCarteira = generatedKeys.getInt(1);
	            }
	            else {
	                throw new SQLException("Creating wallet failed, no ID obtained.");
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idCarteira;
	}
	
	private static int createUser(Connection con, Participante par) {
		System.out.println("Creating user...");
		String sqlQuery = "INSERT INTO Participante (situacao, cpf, nome, idCarteira) VALUES ('" 
				+ par.getSituacao() + "', '" + par.getCpf() + "', '" + par.getNome() + "', " + par.getIdCarteira() + ")";
		PreparedStatement stmt = null;
		int id = -1;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public static Connection getDBConnection() {
		// register the JDBC client driver
		try {
			Driver d = (Driver)Class.forName(JDBC_DRIVER).newInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
		// establish a connection to FileMaker
		Connection con = null;
		try {
			con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void listConnectionWarnings(Connection con) {
		// get connection warnings
		SQLWarning warning = null;
		try {
			warning = con.getWarnings();
			if (warning == null) {
				System.out.println("No warnings...");
			}
			while (warning != null) {
				System.out.println("Warning: "+warning);
				warning = warning.getNextWarning();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static void closeConnection(Connection con) {
		try {
			if(con != null) {
				con.close();
				System.out.println("Connection is closed...");
			}
		} catch(SQLException se) {
			System.out.println("Exception while closing connection...");
	    }
	}
}