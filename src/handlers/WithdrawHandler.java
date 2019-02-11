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
	public static Transaction handleWithdraw(Transaction tran) {
		Connection con = DBHandler.getDBConnection();
		User user = UserHandler.selectUser(con, tran.getUserCpf());
		Wallet wallet = UserHandler.selectUserWallet(con, tran.getUserCpf());
		String type = tran.getType();
		tran.setUserId(user.getId());
		switch (type) {
			case "totalWithdraw": tran.setId(createTotalWithdraw(con, tran, wallet, user));
				break;
//			case "regularContribution": transactionId = createRegularWithdraw(con, tran, wallet, user);
//				break;
//			case "additionalContribution": transactionId = createAdditionalWithdraw(con, tran, wallet, user);
//				break;
//			case "portabilityContribution": transactionId = createPortabilityWithdraw(con, tran, wallet, user);
//				break;
//			case "supplementaryPlanContribution": transactionId = createSupplementaryPlanWithdraw(con, tran, wallet, user);
//				break;
//			case "insuranceCompanyContribution": transactionId = createInsuranceCompanyPlanWithdraw(con, tran, wallet, user);
//				break;
		}
		DBHandler.closeConnection(con);
		return tran;
	}


	private static Boolean checkDateIsValid(Date date) {
//		LocalDate currentDate = Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		LocalDate registrationDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		Integer periodBetween = Period.between(currentDate, registrationDate).getMonths();
//		System.out.println("Period: " + periodBetween);
//		return periodBetween >= 36;
		return true;
	}

	private static Integer createTotalWithdraw(Connection con, Transaction tran, Wallet wallet, User user) {
		System.out.println("Creating transaction...");
		Boolean dateIsValid = checkDateIsValid(user.getRegistrationDate());
		tran.setId(-1);
		if(dateIsValid) {
			Integer userId = tran.getUserId();
			String type = tran.getType();
			Integer numberOfInstallments = 1;
			Integer installmentValue = wallet.calculateTotalBalance();
			tran.setInstallmentValue(installmentValue);
			tran.setNumberOfInstallments(numberOfInstallments);
			try {
				// Cancel user's subscription
				String sqlQuery = String.format("UPDATE \"User\" SET status = 'Canceled' WHERE cpf = '%s'", user.getCpf());
				PreparedStatement prepStmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
				prepStmt.executeUpdate();

				// Empty user's wallet
				sqlQuery = String.format("UPDATE \"Wallet\" SET regularContribution = 0, additionalContribution = 0, portabilityContribution = 0, " +
						"supplementaryPlanContribution = 0, insuranceCompanyContribution = 0 WHERE id IN (SELECT walletId FROM \"User\" WHERE cpf = '%s')", user.getCpf());
				prepStmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
				prepStmt.executeUpdate();

				// Create the Transaction
				sqlQuery = String.format("INSERT INTO \"Transaction\" (userId, type, installmentValue, numberOfInstallments) VALUES (%d, '%s', %d, %d)", userId, type, installmentValue, numberOfInstallments);
				prepStmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
				prepStmt.executeUpdate();
				try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						tran.setId(generatedKeys.getInt(1));
					}
					else {
						throw new SQLException("Deposit failed, no ID obtained.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tran.getId();
	}

}
