package Domain;

public class LogoutOfAccount implements Operation {
    private int number;

    public LogoutOfAccount(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "LogoutOfAccount" +
                "number " + number;
    }

    public void execute() {

    }
}
