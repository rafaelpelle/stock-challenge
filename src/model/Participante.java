package model;

import java.util.Date;

public class Participante {
	private Integer id;
	private Integer idCarteira;
	private Date dataInscricao;
	private String situacao;
	private String cpf;
	private String nome;
	
	public Participante(Integer id, Integer idCarteira, Date dataInscricao, String situacao, String cpf, String nome) {
		this.setId(id);
		this.setIdCarteira(idCarteira);
		this.setDataInscricao(dataInscricao);
		this.setSituacao(situacao);
		this.setCpf(cpf);
		this.setNome(nome);
	}
	
	public Participante(Integer idCarteira, String situacao, String cpf, String nome) {
		this.setIdCarteira(idCarteira);
		this.setSituacao(situacao);
		this.setCpf(cpf);
		this.setNome(nome);
	}

	public Participante(Integer id) {
		this.id = id;
	}
	
	public String toString() {
		return "ID: " + this.getId()
		+ ", CPF: " + this.getCpf()
		+ ", Nome: " + this.getNome()
		+ ", Situa��o: " + this.getSituacao()
		+ ", Data de Inscri��o: " + this.getDataInscricao().toString()
		+ ", ID Carteira: " + this.getIdCarteira();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdCarteira() {
		return idCarteira;
	}

	public void setIdCarteira(Integer idCarteira) {
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
