package handlers;
import model.Transaction;
import model.Wallet;
import model.User;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class WithdrawHandler {
	public static Transaction handleWithdraw(Transaction tran) {
		Connection con = DBHandler.getDBConnection();
		User user = UserHandler.selectUser(con, tran.getUserCpf());
		Wallet wallet = UserHandler.selectUserWallet(con, tran.getUserCpf());
		String type = tran.getType();
		tran.setUserId(user.getId());
		if(type.equals("totalWithdraw")) {
			tran.setId(createTotalWithdraw(con, tran, wallet, user));
		} else {
			tran.setId(createWithdraw(con, tran, wallet, user));
		}
		DBHandler.closeConnection(con);
		return tran;
	}

	private static Boolean checkDateIsValid(User user, Transaction tran) {
		// any kind of withdraw is forbidden during the first 36 months of registration
		LocalDate currentDate = LocalDate.now();
		LocalDate registrationDate = Instant.ofEpochMilli(user.getRegistrationDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		Long monthsBetweenRegistration = ChronoUnit.MONTHS.between(registrationDate, currentDate);

		// regularContribution withdraw (without disconnection from the plan) is available once every two years.
		if(tran.getType().equals("regularContribution")) {
			LocalDate lastRegularWithdraw = Instant.ofEpochMilli(user.getLastRegularWithdraw().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
			Long monthsBetweenLastRegularWithdraw = ChronoUnit.MONTHS.between(lastRegularWithdraw, currentDate);
			return monthsBetweenRegistration >= 36 && monthsBetweenLastRegularWithdraw >= 24;
		} else {
			return monthsBetweenRegistration >= 36;
		}
	}

	private static Integer createTotalWithdraw(Connection con, Transaction tran, Wallet wallet, User user) {
		System.out.println("Creating total withdraw...");
		tran.setId(-1);
		Boolean dateIsValid = checkDateIsValid(user, tran);
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
						throw new SQLException("Withdraw failed, no ID obtained.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tran.getId();
	}

	private static Integer createWithdraw(Connection con, Transaction tran, Wallet wallet, User user) {
		System.out.println("Creating basic withdraw...");
		tran.setId(-1);
		Integer userId = tran.getUserId();
		String type = tran.getType();
		Integer transactionValue = tran.getInstallmentValue() * tran.getNumberOfInstallments();
		Integer availableBalance = wallet.getSpecificBalance(tran.getType());
		Boolean dateIsValid = checkDateIsValid(user, tran);
		// only 20% of regularContribution is available once every two years.
		if (type.equals("regularContribution")) {
			availableBalance = (int)(availableBalance * 0.2);
		}
		if(dateIsValid && availableBalance >= transactionValue) {
			try {
				// Update user's balance
				String sqlQuery = String.format("UPDATE \"Wallet\" SET %s = %s - %d WHERE id IN (SELECT walletId FROM \"User\" WHERE cpf = '%s')", type, type, transactionValue, user.getCpf());
				PreparedStatement prepStmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
				prepStmt.executeUpdate();

				// Update user's last regular withdraw date
				if (type.equals("regularContribution")) {
					sqlQuery = String.format("UPDATE \"User\" SET lastRegularWithdraw = CURRENT_DATE WHERE cpf = '%s'", user.getCpf());
					System.out.println(sqlQuery);
					prepStmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
					prepStmt.executeUpdate();
				}

				// Create the Transaction
				sqlQuery = String.format("INSERT INTO \"Transaction\" (userId, type, installmentValue, numberOfInstallments) VALUES (%d, '%s', %d, %d)", userId, type, tran.getInstallmentValue(), tran.getNumberOfInstallments());
				prepStmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
				prepStmt.executeUpdate();
				try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						tran.setId(generatedKeys.getInt(1));
					}
					else {
						throw new SQLException("Withdraw failed, no ID obtained.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tran.getId();
	}

}
