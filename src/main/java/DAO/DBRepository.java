package DAO;

import Domain.Account;
import Domain.Operation;
import Domain.User;
import UI.RepositoryException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class DBRepository implements Repository {

    private static DBRepository instance;
    private Connection connection;
    private Statement statement;

    public static DBRepository getInstance() throws NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, ClassNotFoundException, IOException {
        if(instance == null){
            instance = new DBRepository();
        }
        return instance;
    }

    public DBRepository() throws NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, ClassNotFoundException, IOException {

        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("src/main/database.properties"))){
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
        init();
    }

    private void init() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS User (" +
                "nameUser VARCHAR(45) PRIMARY KEY NOT NULL UNIQUE," +
                "passwordUser INT NOT NULL)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Account (" +
                "numberAccount INT NOT NULL UNIQUE AUTO_INCREMENT," +
                "ownerAccount VARCHAR(45) NOT NULL," +
                "amountAccount DECIMAL NOT NULL DEFAULT 0, " +
                "PRIMARY KEY(numberAccount, ownerAccount)," +
                "FOREIGN KEY (ownerAccount) REFERENCES User(nameUser) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS UserHistory (" +
                "nameUser VARCHAR(45) NOT NULL," +
                "textOperation VARCHAR(100) NOT NULL," +
                "FOREIGN KEY (nameUser) REFERENCES User(nameUser))");
/*        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Operation (" +
                "idOperation INT PRIMARY KEY NOT NULL UNIQUE," +
                "textOperation VARCHAR(45) NOT NULL)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS OperationInHistory (" +
                "idUserHistory INT PRIMARY KEY NOT NULL," +
                "idOperation INT NOT NULL, " +
                "FOREIGN KEY (idUserHistory) REFERENCES UserHistory (idUserHistory) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (idOperation) REFERENCES Operation (idOperation) ON DELETE CASCADE ON UPDATE CASCADE)");
        statement.executeUpdate("INSERT IGNORE Operation(idOperation, nameOperation) VALUES" +
                "(1,'Create user')," +
                "(2,'Login user')," +
                "(3,'Logout user')," +
                "(4, 'Create account')," +
                "(5, 'Open account')," +
                "(6, 'Logout of account')," +
                "(7, 'Close account')," +
                "(8, 'Deposit')," +
                "(9, 'Withdraw')," +
                "(10, 'Transfer')," +
                "(11, 'Request balance')," +
                "(12, 'Request history')," +
                "(13, 'Request info about accounts')");*/
        statement.executeUpdate("INSERT IGNORE User(nameUser, passwordUser) VALUES" +
                "(1,1)," +
                "(2,2)");
    }

    @Override
    public User getUser(String name) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM User WHERE nameUser="+name+";");
        String userName = null;
        String userPassword = null;
        while(resultSet.next()){
            userName = resultSet.getString("nameUser");
            userPassword = resultSet.getString("passwordUser");
        }
        if(userName != null && userPassword != null)
            return new User(userName, userPassword);
        else return null;
    }

    @Override
    public Account getAccount(int number) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * From Account WHERE numberAccount ="+number+";");
        int numberAccount = -1;
        String ownerAccount = null;
        while(resultSet.next()){
            numberAccount = resultSet.getInt("numberAccount");
            ownerAccount = resultSet.getString("ownerAccount");
        }
        if(numberAccount != -1 && ownerAccount != null)
            return new Account(numberAccount, ownerAccount);
        else return null;
    }

    @Override
    public void addUser(String name, String password) throws SQLException {
        statement.executeUpdate("INSERT User VALUES ("+name+","+password+");");

    }

    @Override
    public void removeUser(String name) throws SQLException {
        statement.executeUpdate("DELETE FROM User WHERE nameUser = "+name+";");
    }

    @Override
    public void addAccount(String owner) throws SQLException {
        statement.executeUpdate("INSERT Account (ownerAccount,amountAccount) VALUES ("+owner+",DEFAULT);");
    }


    @Override
    public void removeAccount(int number, String owner) throws SQLException {
        statement.executeUpdate("DELETE FROM Account WHERE numberAccount = "+number+" and ownerAccount = "+owner+";");
    }

    @Override
    public List getUsersAccounts(String name) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Account WHERE ownerAccount ="+name+";");
        List accountList = new ArrayList();
        while(resultSet.next()){
            accountList.add(new Account(
                    resultSet.getInt("numberAccount"),
                    resultSet.getString("ownerAccount"),
                    resultSet.getBigDecimal("amountAccount")));
        }
        return accountList;
    }

    @Override
    public List getUsersHistory(String name) throws RepositoryException, SQLException {
        List history = new ArrayList();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM UserHistory WHERE nameUser ="+name+";");
        while(resultSet.next()){
            history.add(resultSet.getString("textOperation"));
        }
        if(!history.isEmpty())
            return history;
        else return null;
    }

    @Override
    public void addOperationInHistory(String name, Operation operation) throws SQLException {
/*        ResultSet resultSet = statement.executeQuery("SELECT idUserHistory FROM UserHistory WHERE nameUserHistory = "+name+"");
        int idUserHistory = resultSet.getInt("idUserHistory");
        resultSet = statement.executeQuery("SELECT idOperation FROM Operation WHERE nameOperation ="+operation.toString()+"");
        int idOperation = resultSet.getInt("idOperation");*/
        String str = operation.toString();
        statement.executeUpdate("INSERT UserHistory(nameUser, textOperation) VALUES ("+name+",'"+str+"');");
    }

    @Override
    public boolean isAccountListEmpty(String name) {
        return false;
    }

    @Override
    public void addAmountToAccount(int number, BigDecimal amount) throws SQLException {
        statement.executeUpdate("UPDATE Account SET amountAccount = amountAccount +"+amount+" WHERE numberAccount = "+number+";");
    }

    @Override
    public void removeAmountFromAccount(int number, BigDecimal amount) throws SQLException {
        statement.executeUpdate("UPDATE Account SET amountAccount = amountAccount -"+amount+" WHERE numberAccount = "+number+";");
    }

    @Override
    public void transferAmount(int number, int numberAddresee, BigDecimal amount) throws SQLException {
        Savepoint savepoint = connection.setSavepoint("transferStart");
        try {
            statement.executeUpdate("UPDATE Account SET amountAccount = amountAccount -"+amount+" WHERE numberAccount ="+number+";"+
                    "UPDATE Account SET amountAccount = amountAccount +"+amount+" WHERE numberAccount="+numberAddresee+";");
        } catch (SQLException throwables) {
            connection.rollback(savepoint);
        }

    }


    @Override
    public BigDecimal getAmount(int number) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT amountAccount FROM Account WHERE numberAccount ="+number+";");
        if(resultSet.next())
            return resultSet.getBigDecimal(1);
        return null;
    }
}
