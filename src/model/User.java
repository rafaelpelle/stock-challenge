package model;

import java.util.Date;

public class User {
	private Integer id;
	private Integer walletId;
	private Date registrationDate;
	private Date lastRegularWithdraw;
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
	public Date getLastRegularWithdraw() {
		return this.lastRegularWithdraw;
	}
	public void setLastRegularWithdraw(Date lastRegularWithdraw) {
		this.lastRegularWithdraw = lastRegularWithdraw;
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
