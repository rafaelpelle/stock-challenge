package handlers;
import model.Transaction;

import java.sql.*;

public class WithdrawHandler {
	public static Integer handleWithdraw(Transaction tran) {
		Boolean isPossible = checkWithdrawIsPossible(tran);
		if(isPossible) {
			Connection con = DBHandler.getDBConnection();
			Integer userId = UserHandler.selectUser(con, tran.getUserCpf()).getId();
			tran.setUserId(userId);
			Integer transactionId = createWithdrawTransaction(con, tran);
			withdrawFromUserWallet(con, tran);
			DBHandler.closeConnection(con);
			return transactionId;
		} else {
			return -1;
		}
	}

	private static Boolean checkWithdrawIsPossible(Transaction tran) {
		return true;
		// todo regras de negócio
	}

	// to-do: Duplicated code (DepositHandler.createDepositTransaction)
	private static int createWithdrawTransaction(Connection con, Transaction tran) {
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

	private static Integer withdrawFromUserWallet(Connection con, Transaction tran) {
		System.out.println("Withdrawing from user wallet...");
		String columnName = tran.getType();
		Integer totalValue = tran.getNumberOfInstallments() * tran.getInstallmentValue();
		String cpf = tran.getUserCpf();
		String sqlQuery = String.format("UPDATE \"Wallet\" SET %s = %s - %d WHERE id IN (SELECT walletId FROM \"User\" WHERE cpf = '%s')", columnName, columnName, totalValue, cpf);
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
