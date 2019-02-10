package handlers;
import java.util.Date;
import java.util.ArrayList;
import java.sql.*;

import model.Carteira;
import model.Participante;

public class UserHandler {
	public static Participante handleGetUserByCPF(String cpf) {
		Connection con = DBUtils.getDBConnection();
		Participante par = selectUser(con, cpf);
		DBUtils.closeConnection(con);
		return par;
	}

	public static Carteira handleGetUserWalletByCPF(String cpf) {
		Connection con = DBUtils.getDBConnection();
		Carteira car = selectUserWallet(con, cpf);
		DBUtils.closeConnection(con);
		return car;
	}

	public static Integer handleUserCreation(Participante par) {
		Connection con = DBUtils.getDBConnection();
		Integer idCarteira = createWallet(con);
		par.setSituacao("Ativo");
		par.setIdCarteira(idCarteira);
		Integer id = createUser(con, par);
		DBUtils.closeConnection(con);
		return id;
	}

	private static Carteira selectUserWallet(Connection con, String userCPF) {
		System.out.println("Selecting user...");
		String sqlQuery = String.format("SELECT * FROM Carteira WHERE id IN (SELECT idCarteira FROM Participante WHERE cpf = '%s') ", userCPF);
		Statement stmt = null;
		Carteira car = new Carteira();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				car.setId(rs.getInt("id"));
				car.setContribuicaoNormal(rs.getInt("contribuicaoNormal"));
				car.setContribuicaoAdicional(rs.getInt("contribuicaoAdicional"));
				car.setContribuicaoPortabilidade(rs.getInt("contribuicaoPortabilidade"));
				car.setContribuicaoPlanoPrevComplementar(rs.getInt("contribuicaoPlanoPrevComplementar"));
				car.setContribuicaoSociedadeSeguradora(rs.getInt("contribuicaoSociedadeSeguradora"));
				car.calculateTotalBalance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return car;
	}

	private static Participante selectUser(Connection con, String userCPF) {
		System.out.println("Selecting user...");
		String sqlQuery = String.format("SELECT * FROM Participante WHERE cpf = '%s'", userCPF);
		Statement stmt = null;
		Participante par = new Participante(-1);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				par.setId(rs.getInt("id"));
				par.setIdCarteira(rs.getInt("idCarteira"));
				par.setDataInscricao(rs.getDate("dataInscricao"));
				par.setSituacao(rs.getString("situacao"));
				par.setCpf(rs.getString("cpf"));
				par.setNome(rs.getString("nome"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return par;
	}

	private static ArrayList<Participante> selectAllUsers(Connection con) {
		System.out.println("Selecting user...");
		String sqlQuery = "SELECT * FROM Participante";
		Statement stmt = null;
		ArrayList<Participante> parList = new ArrayList<Participante>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				Integer id = rs.getInt("id");
				Integer idCarteira = rs.getInt("idCarteira");
				Date dataInscricao = rs.getDate("dataInscricao");
				String situacao = rs.getString("situacao");
				String cpf = rs.getString("cpf");
				String nome  = rs.getString("nome");
				parList.add(new Participante(id, idCarteira, dataInscricao, situacao, cpf, nome));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parList;
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
