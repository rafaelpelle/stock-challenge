package handlers;
import modelo.Participante;

import java.sql.*;

public class UserHandler {
	public static Integer handleParticipanteCreation(Participante par) {
		Connection con = DBUtils.getDBConnection();
		DBUtils.listConnectionWarnings(con);
		Integer idCarteira = createWallet(con);
		par.setSituacao("Ativo");
		par.setIdCarteira(idCarteira);
		Integer id = createUser(con, par);
		DBUtils.closeConnection(con);
		return id;
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
}
