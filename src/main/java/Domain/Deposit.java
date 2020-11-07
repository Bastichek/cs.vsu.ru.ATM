package Domain;

import java.math.BigDecimal;

public class Deposit implements Operation {
    private int number;
    private BigDecimal amount;

    public Deposit(int number, BigDecimal amount) {
        this.number = number;
        this.amount = amount;
    }

    public int getNumber() {
        return number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Deposit" +
                "number " + number +
                ", amount " + amount;
    }

    public void execute() {

    }
}
