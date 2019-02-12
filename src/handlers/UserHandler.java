package handlers;
import java.util.Date;
import java.util.ArrayList;
import java.sql.*;
import model.User;
import model.Wallet;

public class UserHandler {
	public static ArrayList<User> handleGetAllUsers() {
		Connection con = DBHandler.getDBConnection();
		ArrayList<User> allUsers = selectAllUsers(con);
		DBHandler.closeConnection(con);
		return allUsers;
	}

	public static User handleGetUserByCPF(String cpf) {
		Connection con = DBHandler.getDBConnection();
		User user = selectUser(con, cpf);
		DBHandler.closeConnection(con);
		return user;
	}

	public static Wallet handleGetUserWalletByCPF(String cpf) {
		Connection con = DBHandler.getDBConnection();
		Wallet wallet = selectUserWallet(con, cpf);
		DBHandler.closeConnection(con);
		return wallet;
	}

	public static Integer handleUserCreation(User user) {
		Connection con = DBHandler.getDBConnection();
		Integer walletId = createWallet(con);
		user.setStatus("Active");
		user.setWalletId(walletId);
		Integer id = createUser(con, user);
		DBHandler.closeConnection(con);
		return id;
	}

	public static Wallet selectUserWallet(Connection con, String userCPF) {
		System.out.println("Selecting user wallet...");
		String sqlQuery = String.format("SELECT * FROM \"Wallet\" WHERE id IN (SELECT walletId FROM \"User\" WHERE cpf = '%s')", userCPF);
		Statement stmt = null;
		Wallet wallet = new Wallet();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				wallet.setId(rs.getInt("id"));
				wallet.setRegularContribution(rs.getInt("regularContribution"));
				wallet.setAdditionalContribution(rs.getInt("additionalContribution"));
				wallet.setPortabilityContribution(rs.getInt("portabilityContribution"));
				wallet.setSupplementaryPlanContribution(rs.getInt("supplementaryPlanContribution"));
				wallet.setInsuranceCompanyContribution(rs.getInt("insuranceCompanyContribution"));
				wallet.calculateTotalBalance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wallet;
	}

	// package-private method
	static User selectUser(Connection con, String userCPF) {
		System.out.println("Selecting user...");
		String sqlQuery = String.format("SELECT * FROM \"User\" WHERE cpf = '%s'", userCPF);
		Statement stmt = null;
		User user = new User(-1);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				user.setId(rs.getInt("id"));
				user.setWalletId(rs.getInt("walletId"));
				user.setRegistrationDate(rs.getDate("registrationDate"));
				user.setLastRegularWithdraw(rs.getDate("lastRegularWithdraw"));
				user.setStatus(rs.getString("status"));
				user.setCpf(rs.getString("cpf"));
				user.setName(rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	private static ArrayList<User> selectAllUsers(Connection con) {
		System.out.println("Selecting all users...");
		String sqlQuery = "SELECT * FROM \"User\"";
		Statement stmt = null;
		ArrayList<User> userList = new ArrayList<User>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				Integer id = rs.getInt("id");
				Integer walletId = rs.getInt("walletId");
				Date registrationDate = rs.getDate("registrationDate");
				String status = rs.getString("status");
				String cpf = rs.getString("cpf");
				String name  = rs.getString("name");
				userList.add(new User(id, walletId, registrationDate, status, cpf, name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}


	private static int createWallet(Connection con) {
		System.out.println("Creating wallet...");
		String sqlQuery = "INSERT INTO \"Wallet\" (regularContribution, additionalContribution, portabilityContribution, " +
				"supplementaryPlanContribution, insuranceCompanyContribution) VALUES (0, 0, 0, 0, 0)";
		PreparedStatement stmt;
		int walletId = -1;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					walletId = generatedKeys.getInt(1);
				}
				else {
					throw new SQLException("Creating wallet failed, no ID obtained.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return walletId;
	}

	private static int createUser(Connection con, User par) {
		System.out.println("Creating user...");
		String sqlQuery = "INSERT INTO \"User\" (status, cpf, name, walletId) VALUES ('"
				+ par.getStatus() + "', '" + par.getCpf() + "', '" + par.getName() + "', " + par.getWalletId() + ")";
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
}
