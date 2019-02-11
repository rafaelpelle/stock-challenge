package model;


public class Wallet {
	private Integer id;
	private Integer regularContribution;
	private Integer additionalContribution;
	private Integer portabilityContribution;
	private Integer supplementaryPlanContribution;
	private Integer insuranceCompanyContribution;
	private Integer totalBalance;
	
	public Wallet() {
		this.id = -1;
		this.regularContribution = 0;
		this.additionalContribution = 0;
		this.portabilityContribution = 0;
		this.supplementaryPlanContribution = 0;
		this.insuranceCompanyContribution = 0;
		this.totalBalance = 0;
	}
	
	public Integer calculateTotalBalance() {
		Integer totalBalance = 0;
		totalBalance += this.regularContribution;
		totalBalance += this.additionalContribution;
		totalBalance += this.portabilityContribution;
		totalBalance += this.supplementaryPlanContribution;
		totalBalance += this.insuranceCompanyContribution;
		this.totalBalance = totalBalance;
		return this.totalBalance;
	}

	public Integer getSpecificBalance(String balanceType) {
		switch(balanceType) {
			case "regularContribution": return this.getRegularContribution();
			case "additionalContribution": return this.getAdditionalContribution();
			case "portabilityContribution": return this.getPortabilityContribution();
			case "supplementaryPlanContribution": return this.getSupplementaryPlanContribution();
			case "insuranceCompanyContribution": return this.getInsuranceCompanyContribution();
			default: return this.calculateTotalBalance();
		}
	}

	public Integer getId() {
		return this.id;
	}
	public Integer getRegularContribution() {
		return this.regularContribution;
	}
	public Integer getAdditionalContribution() {
		return this.additionalContribution;
	}
	public Integer getPortabilityContribution() {
		return this.portabilityContribution;
	}
	public Integer getSupplementaryPlanContribution() {
		return this.supplementaryPlanContribution;
	}
	public Integer getInsuranceCompanyContribution() {
		return this.insuranceCompanyContribution;
	}
	public void setId(Integer value) {
		this.id = value;
	}
	public void setRegularContribution(Integer value) {
		this.regularContribution = value;
	}
	public void setAdditionalContribution(Integer value) {
		this.additionalContribution = value;
	}
	public void setPortabilityContribution(Integer value) {
		this.portabilityContribution = value;
	}
	public void setSupplementaryPlanContribution(Integer value) {
		this.supplementaryPlanContribution = value;
	}
	public void setInsuranceCompanyContribution(Integer value) {
		this.insuranceCompanyContribution = value;
	}
	public void setTotalBalance(Integer value) {
		this.totalBalance = value;
	}
}
