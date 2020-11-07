package Services;

import DAO.DBRepository;
import DAO.Repository;
import Domain.*;
import UI.AtmException;
import UI.RepositoryException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class MainService {
    private Repository<Account> repository;
    private OperationManager operationManager;
    private User mainUser;
    private Account mainAcount;

    public class OperationManager{
        public void closeAccount() throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new CloseAccount(mainAcount.getNumber()));
        }
        public void loginAccount() throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new LoginAccount(mainAcount.getNumber()));
        }
        public void logoutOfAccount() throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new LogoutOfAccount(mainAcount.getNumber()));
        }
        public void logoutOfUser() throws SQLException {repository.addOperationInHistory(mainUser.getLogin(), new LogoutOfUser());}
        public void deposit(int number, BigDecimal amount) throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new Deposit(number, amount));
        }
        public void withDraw(int number, BigDecimal amount) throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new Withdraw(number,amount));
        }
        public void transfer(int number, int numberAddressee, BigDecimal amount) throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new Transfer(number,numberAddressee,amount));
        }
        public void requestBalance(int number) throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new RequestBalance(number));
        }
        public void requestHistory(String login) throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new RequestHistory(login));
        }
        public void requestInfoAboutAccounts() throws SQLException {
            repository.addOperationInHistory(mainUser.getLogin(), new RequestInfoAboutAccounts());
        }
    }

    public MainService(DBRepository dbRepository) {
        operationManager = new OperationManager();
        this.repository = dbRepository;
    }

    public void createNewUser(String name, String password) throws RepositoryException, SQLException {
        repository.addUser(name, password);
    }

    public void deleteUser () throws AtmException, SQLException {
        if(!repository.isAccountListEmpty(mainUser.getLogin())){
            throw new AtmException("Account list is not empty");
        }
        repository.removeUser(mainUser.getLogin());
        mainUser = null;
    }

    public void openAccount() throws AtmException, SQLException {
        if(mainAcount != null){
            throw new AtmException("Already logged");
        }
        repository.addAccount(mainUser.getLogin());
    }

    public void closeAccount() throws AtmException, SQLException {
        if(mainAcount == null){
            throw new AtmException("Not logged");
            }
/*        if (!repository.getUsersAccounts(mainUser.getLogin()).containsKey(mainAcount.getNumber()))
            throw new AtmException("Incorrect account");*/
        repository.removeAccount(mainAcount.getNumber(), mainUser.getLogin());
        operationManager.closeAccount();
        mainAcount = null;
    }

    public void loginUser(String name, String password) throws AtmException, SQLException {
        if(repository.getUser(name) == null) {
            throw new AtmException("User not found");
        }
        User tmpuser = repository.getUser(name);
        if(password.equals(tmpuser.getPassword())){
            mainUser = tmpuser;
        }else{
            throw new AtmException("Wrong password");

        }
    }

    public void logoutOfUser() throws SQLException {
        operationManager.logoutOfUser();
        mainUser = null;
    }

    public void loginAccount(int number) throws AtmException, SQLException {
        if(mainAcount != null){
            throw new AtmException("Already logged");
        }
        List<Account> accounts;
        accounts = repository.getUsersAccounts(mainUser.getLogin());
        for (int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).getNumber() == number && accounts.get(i).getOwner().equals(mainUser.getLogin())){
                mainAcount = accounts.get(i);
                operationManager.loginAccount();
            }
            if(i == accounts.size() - 1 && mainAcount == null)
                throw new AtmException("Account is not found");
        }
        /*if(repository.getUsersAccounts(mainUser.getLogin()).containsKey(number) &&
                mainUser.getLogin().equals
                        (repository.getAccount(number).getOwner())) {
            mainAcount = repository.getUsersAccounts(mainUser.getLogin()).get(number);
            operationManager.loginAccount(mainAcount.getNumber());
        }
        else
        throw new AtmException("Account is not found");*/
    }

    public void logoutOfAccount() throws AtmException, SQLException {
        if(mainAcount == null){
            throw new AtmException("Not logged");
        }
        operationManager.logoutOfAccount();
        mainAcount = null;
    }

    public void deposit (BigDecimal amount) throws AtmException, SQLException {
        if (mainAcount == null){
            throw new AtmException("Not logged");
        }
        repository.addAmountToAccount(mainAcount.getNumber(), amount);
        operationManager.deposit(mainAcount.getNumber(),amount);
    }

    public void withdraw(BigDecimal amount) throws AtmException, SQLException {
        if (mainAcount == null) {
            throw new AtmException("Not logged");

        }
        if(repository.getAmount(mainAcount.getNumber()).subtract(amount).compareTo(new BigDecimal(0)) < 0) {
            throw new AtmException("Not enough money");
        }
        repository.removeAmountFromAccount(mainAcount.getNumber(),amount);
        operationManager.withDraw(mainAcount.getNumber(),amount);
    }

    public void transfer(int numberAddressee, BigDecimal amount) throws AtmException, SQLException {
        if(mainAcount == null){
            throw new AtmException("Not logged");
        }
        if(repository.getAmount(mainAcount.getNumber()).subtract(amount).compareTo(new BigDecimal(0)) < 0) {
            throw new AtmException("Not enough money");
        }
        if(repository.getAccount(numberAddressee) != null ){
            repository.transferAmount(mainAcount.getNumber(),numberAddressee,amount);
            operationManager.transfer(mainAcount.getNumber(),numberAddressee,amount);
        }
        else {
            throw new AtmException("Account is not found");
        }
    }

    public BigDecimal balance () throws AtmException, SQLException {
        if(mainAcount == null){
            throw new AtmException("Not logged");
        }
        operationManager.requestBalance(mainAcount.getNumber());
        return(repository.getAmount(mainAcount.getNumber()));
    }

    public List getHistory() throws RepositoryException, SQLException {
        operationManager.requestHistory(mainUser.getLogin());
        return repository.getUsersHistory(mainUser.getLogin());
    }

    public List<Account> getInfoAboutAccounts() throws SQLException {
        operationManager.requestInfoAboutAccounts();
        return repository.getUsersAccounts(mainUser.getLogin());
    }
}

