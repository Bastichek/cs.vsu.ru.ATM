package Domain;

public class LoginAccount implements Operation {
    private int number;

    public LoginAccount(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "LoginAccount" +
                number;
    }

    public void execute() {

    }
}
