package model;


public class Carteira {
	private Integer id;
	private Integer contribuicaoNormal;
	private Integer contribuicaoAdicional;
	private Integer contribuicaoPortabilidade;
	private Integer contribuicaoPlanoPrevComplementar;
	private Integer contribuicaoSociedadeSeguradora;
	private Integer totalBalance;
	
	public Carteira() {
		this.id = -1;
		this.contribuicaoNormal = 0;
		this.contribuicaoAdicional = 0;
		this.contribuicaoPortabilidade = 0;
		this.contribuicaoPlanoPrevComplementar = 0;
		this.contribuicaoSociedadeSeguradora = 0;
		this.totalBalance = 0;
	}
	
	public Integer calculateTotalBalance() {
		Integer totalBalance = 0;
		totalBalance += this.contribuicaoNormal;
		totalBalance += this.contribuicaoAdicional;
		totalBalance += this.contribuicaoPortabilidade;
		totalBalance += this.contribuicaoPlanoPrevComplementar;
		totalBalance += this.contribuicaoSociedadeSeguradora;
		this.totalBalance = totalBalance;
		return this.totalBalance;
	}

	public Integer getId() {
		return this.id;
	}
	public Integer getContribuicaoNormal() {
		return this.contribuicaoNormal;
	}
	public Integer getContribuicaoAdicional() {
		return this.contribuicaoAdicional;
	}
	public Integer getContribuicaoPortabilidade() {
		return this.contribuicaoPortabilidade;
	}
	public Integer getContribuicaoPlanoPrevComplementar() {
		return this.contribuicaoPlanoPrevComplementar;
	}
	public Integer getContribuicaoSociedadeSeguradora() {
		return this.contribuicaoSociedadeSeguradora;
	}
	public void setId(Integer value) {
		this.id = value;
	}
	public void setContribuicaoNormal(Integer value) {
		this.contribuicaoNormal = value;
	}
	public void setContribuicaoAdicional(Integer value) {
		this.contribuicaoAdicional = value;
	}
	public void setContribuicaoPortabilidade(Integer value) {
		this.contribuicaoPortabilidade = value;
	}
	public void setContribuicaoPlanoPrevComplementar(Integer value) {
		this.contribuicaoPlanoPrevComplementar = value;
	}
	public void setContribuicaoSociedadeSeguradora(Integer value) {
		this.contribuicaoSociedadeSeguradora = value;
	}
	public void setTotalBalance(Integer value) {
		this.totalBalance = value;
	}
}
