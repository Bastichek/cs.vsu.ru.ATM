package Domain;

public class OpenAccount implements Operation {
    private int number;

    public OpenAccount() {
        this.number = number;
    }

    @Override
    public String toString() {
        return "OpenAccount" +
                "number " + number;
    }

    public void execute() {
    }

    @Override
    public int getNumber() {
        return number;
    }

}
