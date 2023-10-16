package rutgersbanking;
import java.time.Year;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;

public class TransactionManager {
    private AccountDatabase accounts;
    private static final int INITIAL_ARR_SIZE = 4;
    private static final int OPEN_ARGS_CC_S = 7; // Arguments expected to open a CC/S account
    private static final int OPEN_ARGS_C_MM = 6; // Arguments expected to open a C/MM account
    private static final int CLOSE_ARGS = 5; // Arguments expected to close an account

    private static final int ACCT_TYPE_INDEX = 1; // Account type index in the parsed command
    private boolean madeAccount = true;
    private boolean madeCloseAccount = true;

    public void run() {
        System.out.println("Transaction Manager is running. \n");
        int commandIteration = 1;
        Account[] accounts = new Account[INITIAL_ARR_SIZE];
        AccountDatabase database = new AccountDatabase(accounts, 0);

        Scanner currentCmd = new Scanner(System.in);
        boolean continueReading = true;

        while (continueReading) {
            continueReading = currentCmd.hasNextLine();

            if(commandIteration == 1){
                System.out.println();
                commandIteration++; // To print an extra line after input is entered
            }

            String curCommand = currentCmd.nextLine();
            boolean stop = commandParser(curCommand, database);

            if (stop) {
                System.out.println("Transaction Manager is terminated.");
                break;
            }
        }
    }


    private boolean commandParser(String command, AccountDatabase database) {
        String[] parsedCommand = command.split("\\s+");
        int counter = 0;
        while (counter < parsedCommand.length) {
            if (checkValid(parsedCommand[counter])) {
                if (parsedCommand[counter].equals("O")) {
                    if (checkNoArgs(parsedCommand)) {
                        counter += openAccount(parsedCommand, database);
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("C")) {
                    if (checkNoArgs(parsedCommand)) {
                        closeAccount(parsedCommand, database);
                       // database.close(makeCloseAccount(parsedCommand));
                        counter += 5;
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("D")) {
                    if (checkNoArgs(parsedCommand)) {
                        database.deposit(makeAccount(parsedCommand));
                        counter += 6;
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("W")) {
                    if (checkNoArgs(parsedCommand)) {
                        database.withdraw(makeAccount(parsedCommand));
                        counter += 6;
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("P")) {
                    database.printSorted();
                    counter += 1;
                } else if (parsedCommand[counter].equals("PI")) {
                    database.printFeesAndInterests();
                    counter += 1;
                } else if (parsedCommand[counter].equals("UB")){
                    database.printUpdatedBalances();
                    counter += 1;
                } else if (parsedCommand[counter].equals("Q")){
                    return true;
                }
            } else counter += parsedCommand.length;
        } return false;
    }

    private int openAccount(String[] command, AccountDatabase database) {
        if (checkProperBalance(command[5])) {
            Account temp = makeAccount(command);
            if (madeAccount) {
                if (database.open(temp)) {
                    System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                            " " + temp.getHolder().getDOB() + "(" + command[1].toUpperCase() + ")" + " opened.");
                } else {
                    System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                            " " + temp.getHolder().getDOB() + "(" + command[1].toUpperCase() + ")" + " is already in the database.");
                }
            }
        }
        if (command[ACCT_TYPE_INDEX].equals("S") || command[ACCT_TYPE_INDEX].equals("CC")) {
            return OPEN_ARGS_CC_S;
        } else {
            return OPEN_ARGS_C_MM;
        }
    }

    private void closeAccount(String [] command, AccountDatabase database){
        Account temp = makeCloseAccount(command);
        if (madeCloseAccount){
            if (database.close(temp)) {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[1].toUpperCase() + ")" + " has been closed.");
            } else {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[1].toUpperCase() + ")" + " is not in the database.");
            }
        }

    }

    private Account makeCloseAccount(String [] commandArg) {
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "C")) {
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    return new Checking(newProfile, 0);
                }
            }
            case "CC" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "CC")){
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    return new CollegeChecking(newProfile, 0, findCampus(2));
                }
            }
            case "S" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "S")) {
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    return new Savings(newProfile, 0, true);
                }
            }
            case "MM" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "MM")){
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    return new MoneyMarket(newProfile, 0, true, 0);
                }
            }
        } madeCloseAccount = false;
        return null;
    }

    private Account makeAccount(String[] commandArg) {
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                madeAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "C")) {
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    return new Checking(newProfile, Double.parseDouble(commandArg[5]));
                }
            }
            case "CC" -> {
                madeAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "CC")){
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    if (checkCampus(commandArg)) // if it's a valid campus
                        return new CollegeChecking(newProfile, Double.parseDouble(commandArg[5]), findCampus(Integer.parseInt(commandArg[6])));
                }
            }
            case "S" -> {
                madeAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                if (checkDate(birthday, "S")) {
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    return new Savings(newProfile, Double.parseDouble(commandArg[5]), Boolean.parseBoolean(commandArg[6]));
                }
            }
            case "MM" -> {
                if (Double.parseDouble(commandArg[5]) < 2000){
                    System.out.println("Minimum of $2000 to open a Money Market account.");
                    madeAccount = false;
                }
                else {
                    madeAccount = true;
                    String[] parsedBday = commandArg[4].split("/");
                    Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                    if (checkDate(birthday, "MM")){
                        Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                        return new MoneyMarket(newProfile, Double.parseDouble(commandArg[5]), true, 0);
                    }
                }
            }
        } madeAccount = false;
        return null;
    }


    /**
     * Try-catch exception for an invalid command.
     * @param commandArg
     * @return
     */
    private boolean checkValid(String commandArg){
        String [] acceptableCmd = {"O", "C", "D", "W", "P", "PI", "UB", "Q"};
        boolean validCmd = false;

        for (int i = 0; i < acceptableCmd.length; i++){
            if (commandArg.equals(acceptableCmd[i])){
                validCmd = true;
            }
        }

        try {
            if (!validCmd && !commandArg.equals("")){
                throw new IllegalArgumentException("Invalid command!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        if (commandArg.equals("")){
            return false;
        }

        return true;
    }

    /**
     * Try-catch IllegalArgumentException for an invalid command.
     * @param commandArgAcctType
     * @return
     */
    private boolean checkAccountTypeValid(String commandArgAcctType){
        String [] acceptableAcct = {"C", "CC", "S", "MM"};
        boolean validCmd = false;

        for (int i = 0; i < acceptableAcct.length; i++){
            if (commandArgAcctType.equals(acceptableAcct[i])){
                validCmd = true;
            }
        }

        try {
            if (!validCmd){
                throw new IllegalArgumentException("Invalid command!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    private boolean checkProperBalance(String balance) {
        try {
            Double testing = Double.parseDouble(balance);
            if (testing <= 0) {
                System.out.println("Initial deposit cannot be 0 or negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Not a valid amount.");
            return false;
        }
        return true;
    }


    /**
     * Try-catch InputMismatchException for insufficient input arguments.
     * @param command
     * @return
     */

    private boolean checkNoArgs(String[] command){
        String cmdType = command[0];
        String acctType = null;
        int numArgs = command.length;
        try {
            if (cmdType.equals("O")){
                if (numArgs < 6) {
                    throw new InputMismatchException("Missing data for opening an account.");
                } else {
                    acctType = command[1];
                }
                if (acctType.equals("CC") || acctType.equals("S")) {
                    if (numArgs != OPEN_ARGS_CC_S) {
                        throw new InputMismatchException("Missing data for opening an account.");
                    }
                } else if (acctType.equals("C") || acctType.equals("MM")){
                    if (numArgs != OPEN_ARGS_C_MM){
                        throw new InputMismatchException("Missing data for opening an account.");
                    }
                }
            } else if (cmdType.equals("C")){
                if (numArgs != CLOSE_ARGS){
                    throw new InputMismatchException("Missing data for closing an account.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean checkCampus(String [] commandArg){
        int campusCode = Integer.parseInt(commandArg[6]);
        if (campusCode != 0 && campusCode != 1 && campusCode != 2){
            System.out.println("Invalid campus code.");
            return false;
        }
        Campus campus = Campus.NEWARK;
        for (Campus check: Campus.values()) {
            if (check.getCode() == campusCode) {
                campus = check;
                return true;
            }
        }

        return false;
    }

    private Campus findCampus(int campusCode){
        Campus campus = Campus.NEWARK;
        for (Campus check: Campus.values()) {
            if (check.getCode() == campusCode) {
                campus = check;
            }
        }

        return campus;
    }

    private boolean checkDate(Date bday, String accountType){
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int todaysDay = currentDate.get(Calendar.DAY_OF_MONTH);
        Profile temp = new Profile("Temp", "Temp", bday);

        if (bday.getYear() == currentYear){
            System.out.println("DOB invalid: " + bday + " cannot be today or a future day.");
            return false;
        } else if (!bday.isValid()){
            System.out.println("DOB invalid: " + bday + " not a valid calendar date!");
            return false;
        } else if (accountType.equals("CC")){
            if (temp.age() < 16 ){
                System.out.println("DOB invalid: " + bday + " under 16.");
                return false;
            } else if (temp.age() > 24){
                System.out.println("DOB invalid: " + bday + " over 24.");
                return false;
            } else if (temp.age() == 24){
                if(bday.getDay() < todaysDay){
                    System.out.println("DOB invalid: " + bday + " over 24.");
                    return false;
                }
            }
        } else if (!accountType.equals("CC")){
            if (temp.age() < 16 ){
                System.out.println("DOB invalid: " + bday + " under 16.");
                return false;
            }
        }

        return true;
    }

}
