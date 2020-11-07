package DAO;

import Domain.Account;
import Domain.Operation;
import Domain.User;
import UI.RepositoryException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HardCodeRepository implements Repository<Account>{

    private static HardCodeRepository instance;

    private Map<String, User> userList = new HashMap<String,User>();
    private Map<String, HashMap<Integer, Account>> usersAccounts = new HashMap<>();
    private Map<String, ArrayList<Operation>> usersHistory = new HashMap<>();
    private Map<Integer, Account> allAcountsList = new HashMap<Integer, Account>();
    private AtomicInteger number;

    private HardCodeRepository(){
        init();
    }

    public static HardCodeRepository getInstance(){
        if(instance == null){
            instance = new HardCodeRepository();
        }
        return instance;
    }

    public User getUser(String name) {
        return userList.getOrDefault(name, null);
    }
    public Account getAccount(int number){
        return allAcountsList.get(number);
    }
    public List getUsersAccounts(String name) {
/*        if(!usersAccounts.get(name).isEmpty()){
            return usersAccounts;
        }
        else return null;*/
        return null;
    }

    public boolean isAccountListEmpty (String name){
        if (usersAccounts.get(name).isEmpty())
            return true;
        else return false;
    }

    @Override
    public void addAmountToAccount(int number, BigDecimal amount) {

    }

    @Override
    public void removeAmountFromAccount(int number, BigDecimal amount) {

    }

    @Override
    public void transferAmount(int number, int numberAddresee, BigDecimal amount) throws SQLException {

    }

    @Override
    public BigDecimal getAmount(int number) throws SQLException {
        return null;
    }

    public void addOperationInHistory(String name, Operation operation){
        usersHistory.get(name).add(operation);
    }

    public List<Operation> getUsersHistory(String name) throws RepositoryException {
        if (usersHistory.get(name).isEmpty()){
            throw new RepositoryException("History is empty");
        }
        return usersHistory.get(name);
    }

    public void addUser(String name, String password) throws RepositoryException {
        if (userList.containsKey(name)) {
            throw new RepositoryException("User already exists");
        } else {
            userList.put(name, new User(name,password));
            usersAccounts.put(name, new HashMap<>());
            usersHistory.put(name, new ArrayList<>());
        }
    }

    public void removeUser(String name) {
        userList.remove(name);
    }

    public void addAccount(String owner) {
        Account account = new Account(number.get(),owner);
        usersAccounts.get(owner).put(number.get(), account);
        allAcountsList.put(number.get(), account);
    }

    public void removeAccount(int number, String owner) {
        usersAccounts.get(owner).remove(number);
        allAcountsList.remove(number);
    }



    private void init(){


    }
}
