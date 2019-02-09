package modelo;

import java.util.Date;

public class Participante {
	private int id;
	private int idCarteira;
	private Date dataInscricao;
	private String situacao;
	private String cpf;
	private String nome;
	
	public Participante(int id, int idCarteira, Date dataInscricao, String situacao, String cpf, String nome) {
		this.setId(id);
		this.setIdCarteira(idCarteira);
		this.setDataInscricao(dataInscricao);
		this.setSituacao(situacao);
		this.setCpf(cpf);
		this.setNome(nome);
	}
	
	public Participante(int idCarteira, String situacao, String cpf, String nome) {
		this.setIdCarteira(idCarteira);
		this.setSituacao(situacao);
		this.setCpf(cpf);
		this.setNome(nome);
	}
	
	public String toString() {
		return "ID: " + this.getId()
		+ ", CPF: " + this.getCpf()
		+ ", Nome: " + this.getNome()
		+ ", Situação: " + this.getSituacao()
		+ ", Data de Inscrição: " + this.getDataInscricao().toString()
		+ ", ID Carteira: " + this.getIdCarteira();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCarteira() {
		return idCarteira;
	}

	public void setIdCarteira(int idCarteira) {
		this.idCarteira = idCarteira;
	}

	public Date getDataInscricao() {
		return dataInscricao;
	}

	public void setDataInscricao(Date dataInscricao) {
		this.dataInscricao = dataInscricao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
