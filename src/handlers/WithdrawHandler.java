package handlers;
import model.Transaction;
import model.Wallet;
import model.User;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class WithdrawHandler {
	public static Integer handleWithdraw(Transaction tran) {
		Connection con = DBHandler.getDBConnection();
		User user = UserHandler.selectUser(con, tran.getUserCpf());
		Wallet wallet = UserHandler.selectUserWallet(con, tran.getUserCpf());
		tran.setUserId(user.getId());
		Integer transactionId = -1;
		String type = tran.getType();
		switch (type) {
			case "totalWithdraw": transactionId = createTotalWithdraw(con, tran, wallet, user);
				break;
			case "regularContribution": transactionId = createRegularWithdraw(con, tran, wallet, user);
				break;
			case "additionalContribution": transactionId = createAdditionalWithdraw(con, tran, wallet, user);
				break;
			case "portabilityContribution": transactionId = createPortabilityWithdraw(con, tran, wallet, user);
				break;
			case "supplementaryPlanContribution": transactionId = createSupplementaryPlanWithdraw(con, tran, wallet, user);
				break;
			case "insuranceCompanyContribution": transactionId = createInsuranceCompanyPlanWithdraw(con, tran, wallet, user);
				break;
		}
		DBHandler.closeConnection(con);
		return transactionId;
	}


	private static Boolean checkDateIsValid(Date date) {
		LocalDate currentDate = Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate registrationDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer periodBetween = Period.between(currentDate, registrationDate).getMonths();
		System.out.println("Period: " + periodBetween);
		return  periodBetween >= 36;
	}

	private static Integer createTotalWithdraw(Connection con, Transaction tran, Wallet wallet, User user) {
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

//	private static int createWithdrawTransaction(Connection con, Transaction tran) {
//		System.out.println("Creating transaction...");
//		Integer userId = tran.getUserId();
//		String type = tran.getType();
//		Integer numberOfInstallments = tran.getNumberOfInstallments();
//		Integer installmentValue = tran.getInstallmentValue();
//		String sqlQuery = String.format("INSERT INTO \"Transaction\" (userId, type, installmentValue, numberOfInstallments) VALUES (%d, '%s', %d, %d)", userId, type, installmentValue, numberOfInstallments);
//		PreparedStatement stmt;
//		int transactionId = -1;
//		try {
//			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
//			stmt.executeUpdate();
//			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//				if (generatedKeys.next()) {
//					transactionId = generatedKeys.getInt(1);
//				}
//				else {
//					throw new SQLException("Creating transaction failed, no ID obtained.");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return transactionId;
//	}

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
