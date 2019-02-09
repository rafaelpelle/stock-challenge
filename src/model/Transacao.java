package model;
import java.util.Date;

public class Transacao {
    private Integer id;
    private Integer valorParcela;
    private Integer qtdParcelas;
    private String tipo;
    private Date data;

    public Transacao(Integer valorParcela, Integer qtdParcelas, String tipo) {
        this.valorParcela = valorParcela;
        this.qtdParcelas = qtdParcelas;
        this.tipo = tipo;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValorParcela() {
        return this.valorParcela;
    }

    public void setValorParcela(Integer valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Integer getQtdParcelas() {
        return this.qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
