package Domain;

public class RequestHistory implements Operation {
    private String login;

    public RequestHistory(String login) {
        this.login = login;
    }
    @Override
    public String toString() {
        return "RequestHistory ";
    }

    public void execute() {

    }

    @Override
    public int getNumber() {
        return 0;
    }
}
