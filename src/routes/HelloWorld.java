package routes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.Participante;


@Path("/helloworld")
public class HelloWorld {
	private static final String JDBC_DRIVER = "com.filemaker.jdbc.Driver";
	private static final String DB_URL = "jdbc:filemaker://localhost/database";
	private static final String DB_USER = "Admin";
	private static final String DB_PASS = "password";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHelloWorld() {
		Connection con = getDBConnection();
		listConnectionWarnings(con);
		int idCarteira = createWallet(con);
		Participante par = new modelo.Participante(idCarteira, "Ativo", "08194709962", "Rafael Pelle");
		int id = createUser(con, par);
		closeConnection(con);
		System.out.println("Finished...");
		String jsonString = "{\"id\":\"" + id + "\", \"idCarteira\":" + idCarteira + "}";
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return Response.ok(jsonString).build();
	}

	private static int createWallet(Connection con) {
		System.out.println("Creating wallet...");
		String sqlQuery = "INSERT INTO Carteira (contribuicaoNormal, contribuicaoAdicional, contribuicaoPortabilidade, " +
				"contribuicaoPlanoPrevComplementar, contribuicaoSociedadeSeguradora) VALUES (0, 0, 0, 0, 0)";
		PreparedStatement stmt;
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

	private static int createUser(Connection con, modelo.Participante par) {
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

	private static Connection getDBConnection() {
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

	private static void listConnectionWarnings(Connection con) {
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
			e.printStackTrace();
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
