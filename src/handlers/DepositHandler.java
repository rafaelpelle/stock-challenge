package handlers;
import model.Transaction;

import java.sql.*;

public class DepositHandler {
	public static Integer handleDeposit(Transaction tran) {
		Connection con = DBHandler.getDBConnection();
		Integer userId = UserHandler.selectUser(con, tran.getUserCpf()).getId();
		tran.setUserId(userId);
		Integer transactionId = createDepositTransaction(con, tran);
		depositToUserWallet(con, tran);
		DBHandler.closeConnection(con);
		return transactionId;
	}

	// to-do: Duplicated code (WithdrawHandler.createWithdrawTransaction)
	private static int createDepositTransaction(Connection con, Transaction tran) {
		System.out.println("Creating transaction...");
		Integer userId = tran.getUserId();
		String type = tran.getType();
		Integer numberOfInstallments = tran.getNumberOfInstallments();
		Integer installmentValue = tran.getInstallmentValue();
		String sqlQuery = String.format("INSERT INTO \"Transaction\" (userId, type, installmentValue, numberOfInstallments) VALUES (%d, '%s', %d, %d)", userId, type, installmentValue, numberOfInstallments);
		PreparedStatement stmt;
		int transactionId = -1;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					transactionId = generatedKeys.getInt(1);
				}
				else {
					throw new SQLException("Creating transaction failed, no ID obtained.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionId;
	}

	private static Integer depositToUserWallet(Connection con, Transaction tran) {
		System.out.println("Depositing to user wallet...");
		String columnName = tran.getType();
		Integer totalValue = tran.getNumberOfInstallments() * tran.getInstallmentValue();
		String cpf = tran.getUserCpf();
		String sqlQuery = String.format("UPDATE \"Wallet\" SET %s = %s + %d WHERE id IN (SELECT walletId FROM \"User\" WHERE cpf = '%s')", columnName, columnName, totalValue, cpf);
		PreparedStatement stmt;
		int depositId = -1;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					depositId = generatedKeys.getInt(1);
				}
				else {
					throw new SQLException("Deposit failed, no ID obtained.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return depositId;
	}
}
