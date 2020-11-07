package Domain;

import java.math.BigDecimal;

public class Transfer implements Operation {
    private int number;
    private int numberAddressee;
    private BigDecimal amount;

    public Transfer(int number, int numberAddressee, BigDecimal amount) {
        this.number = number;
        this.numberAddressee = numberAddressee;
        this.amount = amount;
    }

    public int getNumber() {
        return number;
    }

    public int getNumberAddressee() {
        return numberAddressee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transfer" +
                "number " + number +
                ", numberAddressee " + numberAddressee +
                ", amount " + amount;
    }

    public void execute() {

    }
}
