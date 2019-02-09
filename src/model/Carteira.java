package model;


public class Carteira {
	private int id;
	private int contribuicaoNormal;
	private int contribuicaoAdicional;
	private int contribuicaoPortabilidade;
	private int contribuicaoPlanoPrevComplementar;
	private int contribuicaoSociedadeSeguradora;
	
	public Carteira() {
		this.id = -1;
		this.contribuicaoNormal = 0;
		this.contribuicaoAdicional = 0;
		this.contribuicaoPortabilidade = 0;
		this.contribuicaoPlanoPrevComplementar = 0;
		this.contribuicaoSociedadeSeguradora = 0;
	}
	
	public int calcularSaldoTotal() {
		int saldoTotal = 0;
		saldoTotal += this.contribuicaoNormal;
		saldoTotal += this.contribuicaoAdicional;
		saldoTotal += this.contribuicaoPortabilidade;
		saldoTotal += this.contribuicaoPlanoPrevComplementar;
		saldoTotal += this.contribuicaoSociedadeSeguradora;
		return saldoTotal;
	}
}
