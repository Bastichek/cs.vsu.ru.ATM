package Domain;


import java.math.BigDecimal;

public class Account {
    private int number;
    private String owner;
    private BigDecimal amount;

    public Account(int number, String ownwer) {
        this.number = number;
        this.owner = ownwer;


    }

    public Account(int number, String ownwer, BigDecimal amount) {
        this.number = number;
        this.owner = ownwer;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

}
