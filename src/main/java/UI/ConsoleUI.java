package UI;

import Domain.Account;
import Services.MainService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private MainService mainService;
    private int user_choice = -1;
    private Scanner scanner = new Scanner(System.in);
    private BigDecimal amount;

    public ConsoleUI(MainService atm) {
        this.mainService = atm;
    }

    public void mainMenu() throws SQLException {
        do {
            System.out.println("1) Create a new user account");
            System.out.println("2) Login to user account");
            System.out.println("0) Quit");
            System.out.println();
            System.out.print("Enter choice [0-2]: ");

            user_choice = scanner.nextInt();
            switch (user_choice) {
                case 1:
                    System.out.println("Enter name and password");
                    String name = scanner.next();
                    String password = scanner.next();
                    try {
                        mainService.createNewUser(name, password);
                        System.out.println("User was created, name: " + name + "\npassword: " + password);
                    } catch (RepositoryException | SQLException e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 2:
                    System.out.println("Enter name and password");
                    name = scanner.next();
                    password = scanner.next();
                    try {
                        mainService.loginUser(name, password);
                        System.out.println("Was logged, name: " + name);
                    } catch (AtmException | SQLException e) {
                        System.out.println(e.toString());
                        break;
                    }
                    userMenu();
                    break;
                case 0:
                    System.exit(0);
            }
        } while (user_choice != '0');
    }

    public void userMenu() throws SQLException {

        do {
            System.out.println("1) Create a new bank account");
            System.out.println("2) Login to bank account");
            System.out.println("3) Logout of bank account");
            System.out.println("4) Deposit");
            System.out.println("5) Withdraw");
            System.out.println("6) Transfer");
            System.out.println("7) Balance");
            System.out.println("8) History");
            System.out.println("9) Print info about accounts");
            System.out.println("10) Close account");
            System.out.println("11) Delete user");
            System.out.println("0) Logout");
            System.out.println();
            System.out.print("Enter choice [0-10]: ");
            user_choice = scanner.nextInt();
            switch (user_choice) {
                case 1:
                    try {
                        mainService.openAccount();
                        System.out.println("Account was opened");
                    } catch (AtmException | SQLException e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 2:
                    System.out.println("Enter account number");
                    int number = scanner.nextInt();
                    try {
                        mainService.loginAccount(number);
                        System.out.println("Account number: " + number);
                    } catch (AtmException e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 3:
                    try {
                        mainService.logoutOfAccount();
                        System.out.println("Was logout");
                    } catch (AtmException e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 4:
                    System.out.println("Enter amount");
                    amount = scanner.nextBigDecimal();
                    try {
                        mainService.deposit(amount);
                        System.out.println("Deposit: " + amount);
                        amount = null;
                    } catch (AtmException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    System.out.println("Enter amount");
                    amount = scanner.nextBigDecimal();
                    try {
                        mainService.withdraw(amount);
                        System.out.println("Withdraw: " + amount);
                        amount = null;
                    } catch (AtmException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("Enter number addressee and amount");
                    int numberAddressee = scanner.nextInt();
                    amount = scanner.nextBigDecimal();
                    try {
                        mainService.transfer(numberAddressee, amount);
                        System.out.println("Transfer to: " + numberAddressee + " Amount: " + amount);
                        amount = null;
                    } catch (AtmException e) {
                        e.printStackTrace();
                    }

                    break;
                case 7:
                    try {
                        System.out.println("Balance: "+ mainService.balance());
                    } catch (AtmException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    List history = null;
                    try {
                        history = mainService.getHistory();
                    } catch (RepositoryException e) {
                        System.out.println(e.toString());                    }
                    if(history == null){
                        break;
                    }
                    for (Object operation:history
                         ) {
                        System.out.println(operation.toString());
                    }
                    break;
                case 9:
                    List<Account> infoAboutAccounts = mainService.getInfoAboutAccounts();
                    for (int i = 0; i < infoAboutAccounts.size(); i++) {
                        System.out.println("Number:" +
                                infoAboutAccounts.get(i).getNumber() +
                                " Amount: " +
                                infoAboutAccounts.get(i).getAmount());
                    }
                    break;
                case 10:
                    try {
                        mainService.closeAccount();
                        System.out.println("Account was closed");
                    } catch (AtmException | SQLException e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 11:
                    try {
                        mainService.deleteUser();
                        System.out.println("User was deleted");
                    } catch (AtmException | SQLException e) {
                        System.out.println(e.toString());
                    }
                    break;
                case 0:
                    mainService.logoutOfUser();
                    return;

            }
        }
        while (user_choice != '0');
    }
}
