import DAO.DBRepository;
import DAO.HardCodeRepository;
import Services.MainService;
import UI.ConsoleUI;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try{
            MainService mainService = new MainService(DBRepository.getInstance());
            ConsoleUI consoleUI = new ConsoleUI(mainService);
            System.out.println("Connection succesfull!");
            consoleUI.mainMenu();
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }
}

