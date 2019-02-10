package model;
import java.util.Date;

public class Transaction {
    private Integer id;
    private Integer userId;
    private String userCpf;
    private String type;
    private Date date;
    private Integer installmentValue;
    private Integer numberOfInstallments;

    public Transaction(Integer installmentValue, Integer numberOfInstallments, String type) {
        this.installmentValue = installmentValue;
        this.numberOfInstallments = numberOfInstallments;
        this.type = type;
    }

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return this.userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserCpf() {
        return this.userCpf;
    }
    public void setUserCpf(String userCpf) {
        this.userCpf = userCpf;
    }
    public Integer getInstallmentValue() {
        return this.installmentValue;
    }
    public void setInstallmentValue(Integer installmentValue) {
        this.installmentValue = installmentValue;
    }
    public Integer getNumberOfInstallments() {
        return this.numberOfInstallments;
    }
    public void setNumberOfInstallments(Integer numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
