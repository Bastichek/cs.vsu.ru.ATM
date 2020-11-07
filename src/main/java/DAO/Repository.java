package DAO;



import Domain.Account;
import Domain.Operation;
import Domain.User;
import UI.RepositoryException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Repository<E> {

    User getUser(String name) throws SQLException;
    Account getAccount(int number) throws SQLException;
    void addUser(String name, String password) throws RepositoryException, SQLException;
    void removeUser(String name) throws SQLException;
    void addAccount(String owner) throws SQLException;
    void removeAccount(int number, String owner) throws SQLException;
    List<Account> getUsersAccounts(String name) throws SQLException;
    List<Operation> getUsersHistory(String name) throws RepositoryException, SQLException;
    void addOperationInHistory(String name, Operation operation) throws SQLException;
    boolean isAccountListEmpty (String name);
    void addAmountToAccount(int number, BigDecimal amount) throws SQLException;
    void removeAmountFromAccount(int number, BigDecimal amount) throws SQLException;
    void transferAmount(int number, int numberAddresee, BigDecimal amount) throws SQLException;
    BigDecimal getAmount(int number) throws SQLException;
    }
