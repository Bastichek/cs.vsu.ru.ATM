package Domain;

public class RequestBalance implements Operation {
    private int number;

    public RequestBalance(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "RequestBalance" +
                "number " + number;
    }

    public void execute() {

    }
}
