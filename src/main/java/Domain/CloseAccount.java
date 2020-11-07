package Domain;

public class CloseAccount implements Operation {
    private int number;

    public CloseAccount(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "CloseAccount" +
                "number " + number;
    }

    public void execute() {

    }
}
