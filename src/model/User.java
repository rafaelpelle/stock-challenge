package model;

import java.util.Date;

public class User {
	private Integer id;
	private Integer walletId;
	private Date registrationDate;
	private String status;
	private String cpf;
	private String name;
	
	public User(Integer id, Integer walletId, Date registrationDate, String status, String cpf, String name) {
		this.setId(id);
		this.setWalletId(walletId);
		this.setRegistrationDate(registrationDate);
		this.setStatus(status);
		this.setCpf(cpf);
		this.setName(name);
	}
	
	public User(Integer walletId, String status, String cpf, String name) {
		this.setWalletId(walletId);
		this.setStatus(status);
		this.setCpf(cpf);
		this.setName(name);
	}

	public User(Integer id) {
		this.id = id;
	}
	
	public String toString() {
		return "ID: " + this.getId()
		+ ", CPF: " + this.getCpf()
		+ ", Nome: " + this.getName()
		+ ", Situa��o: " + this.getStatus()
		+ ", Data de Inscri��o: " + this.getRegistrationDate().toString()
		+ ", ID Wallet: " + this.getWalletId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWalletId() {
		return walletId;
	}

	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
