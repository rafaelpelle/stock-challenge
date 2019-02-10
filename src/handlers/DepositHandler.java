package handlers;
import model.Transacao;

import java.sql.*;

public class DepositHandler {
	public static Integer handleDeposit(Transacao tran) {
		Connection con = DBHandler.getDBConnection();
		Integer idTransacao = createTransaction(con, tran);
		Integer idParticipante = UserHandler.selectUser(con, tran.getCpfParticipante()).getId();
		linkTransacionAndUser(con, idTransacao, idParticipante);
		depositInUserWallet(con, tran);
		DBHandler.closeConnection(con);
		return idTransacao;
	}

	private static int createTransaction(Connection con, Transacao tran) {
		System.out.println("Creating transaction...");
		String tipo = tran.getTipo();
		Integer qtdParcelas = tran.getQtdParcelas();
		Integer valorParcela = tran.getValorParcela();
		String sqlQuery = String.format("INSERT INTO Transacao (tipo, valorParcela, qtdParcelas) VALUES ('%s', %d, %d)", tipo, valorParcela, qtdParcelas);
		PreparedStatement stmt;
		int idTransacao = -1;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					idTransacao = generatedKeys.getInt(1);
				}
				else {
					throw new SQLException("Creating transaction failed, no ID obtained.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idTransacao;
	}

	private static void linkTransacionAndUser(Connection con, Integer idTransacao, Integer idParticipante) {
		System.out.println("Creating link between transaction and user...");
		// to-do: Da pra pegar o valor de Participante.id e inserir em Transacao_Participante.idParticipante na mesma query?
		// a query a seguir retorna uma exceção do FileMaker reclamando da sintaxe.
		// String sqlQuery = String.format("INSERT INTO Transacao_Participante (idParticipante, idTransacao) SELECT id, %d FROM Participante WHERE Participante.cpf = '%s'", idTransacao, cpf);
		String sqlQuery = String.format("INSERT INTO Transacao_Participante (idParticipante, idTransacao) VALUES (%d, %d)", idParticipante, idTransacao);
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Integer depositInUserWallet(Connection con, Transacao tran) {
		System.out.println("Depositing in user wallet...");
		String columnName = tran.getTipo();
		Integer totalValue = tran.getQtdParcelas() * tran.getValorParcela();
		String cpf = tran.getCpfParticipante();
		String sqlQuery = String.format("UPDATE Carteira SET %s = %s + %d WHERE id IN (SELECT idCarteira FROM Participante WHERE cpf = '%s')", columnName, columnName, totalValue, cpf);
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
