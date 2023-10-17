package rutgersbanking;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;

/**
 * User interface to perform account operations.
 * @author Haejin Song, Deshna Doshi
 */

public class TransactionManager {
    private AccountDatabase accounts;
    private static final int INITIAL_ARR_SIZE = 4;
    private static final int OPEN_ARGS_CC_S = 7; // Arguments expected to open a CC/S account
    private static final int OPEN_ARGS_C_MM = 6; // Arguments expected to open a C/MM account
    private static final int CLOSE_ARGS = 5; // Arguments expected to close an account

    private static final int ACCT_TYPE_INDEX = 1; // Account type index in the parsed command
    private boolean madeAccount = false;
    private boolean madeCloseAccount = false;
    private boolean madeDepAccount = false;
    private static final int INTIAL_CMD = 1;
    private static final int PRINT_ARGS = 1;

    private static final int BALANCE_IND = 5;
    private static final int DATE_IND = 1;
    private static final int FNAME_IND = 2;
    private static final int LNAME_IND = 3;
    private static final int BDAY_IND = 4;

    private static final int DAY = 1;
    private static final int MONTH = 0;
    private static final int YR = 2;

    private static final int STATUS_IND = 6;
    private static final int MM_MIN = 2000;
    private static final int INVALID_DEP = 0;

    private static final int MIN_AGE = 16;
    private static final int MAX_AGE = 24;



    /**
     * User interface to read inputs and commands.
     */
    public void run() {
        System.out.println("Transaction Manager is running. \n");
        int commandIteration = 1;
        Account[] accounts = new Account[INITIAL_ARR_SIZE];
        AccountDatabase database = new AccountDatabase(accounts, 0);

        Scanner currentCmd = new Scanner(System.in);
        boolean continueReading = true;

        while (continueReading) {
            continueReading = currentCmd.hasNextLine();

            if(commandIteration == INTIAL_CMD){
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

    /**
     * Separates commands based on lines and spaces, to read input.
     * @param command the String command as taken from the run() method.
     * @param database the Account database that holds all of the accounts.
     * @return true if the program is to be terminated, false otherwise.
     */
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
                        counter += CLOSE_ARGS;
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("D")) {
                    if (checkNoArgs(parsedCommand)) {
                        depositAccount(parsedCommand, database);
                        counter += OPEN_ARGS_C_MM;
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("W")) {
                    if (checkNoArgs(parsedCommand)) {
                        withdrawAccount(parsedCommand, database);
                        counter += OPEN_ARGS_C_MM;
                    } else counter += parsedCommand.length;
                } else if (parsedCommand[counter].equals("P")) {
                    database.printSorted();
                    counter += PRINT_ARGS;
                } else if (parsedCommand[counter].equals("PI")) {
                    database.printFeesAndInterests();
                    counter += PRINT_ARGS;
                } else if (parsedCommand[counter].equals("UB")){
                    database.printUpdatedBalances();
                    counter += PRINT_ARGS;
                } else if (parsedCommand[counter].equals("Q")){
                    return true;
                }
            } else counter += parsedCommand.length;
        } return false;
    }

    /**
     * Opens an account given that it meets the requirements.
     * @param command the String command as taken from the run() method.
     * @param database the Account database that holds all of the accounts.
     * @return the number of arguments expected for the command.
     */
    private int openAccount(String[] command, AccountDatabase database) {
        if (checkProperBalance(command[BALANCE_IND])) {
            Account temp = makeAccount(command);
            if (madeAccount) {
                if (database.open(temp)) {
                    System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                            " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " opened.");
                } else {
                    System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                            " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " is already in the database.");
                }
            }
        }
        if (command[ACCT_TYPE_INDEX].equals("S") || command[ACCT_TYPE_INDEX].equals("CC")) {
            return OPEN_ARGS_CC_S;
        } else {
            return OPEN_ARGS_C_MM;
        }
    }

    /**
     * Closes an account given it meets the requirements.
     * @param command the String command as taken from the run() method.
     * @param database the Account database that holds all of the accounts.
     */
    private void closeAccount(String [] command, AccountDatabase database){
        Account temp = makeCloseAccount(command);
        if (madeCloseAccount){
            if (database.close(temp)) {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " has been closed.");
            } else {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " is not in the database.");
            }
        }

    }


    /**
     * Creates a shell account used to delete an existing account.
     * @param commandArg The parsed command taken from the input.
     * @return the shell Account object.
     */
    private Account makeCloseAccount(String [] commandArg) {
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                if (checkDate(birthday, "C")) {
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    return new Checking(newProfile, 0);
                }
            }
            case "CC" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                if (checkDate(birthday, "CC")){
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    return new CollegeChecking(newProfile, 0, findCampus(2));
                }
            }
            case "S" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                if (checkDate(birthday, "S")) {
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    return new Savings(newProfile, 0, true);
                }
            }
            case "MM" -> {
                madeCloseAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                if (checkDate(birthday, "MM")){
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    return new MoneyMarket(newProfile, 0, true, 0);
                }
            }
        } madeCloseAccount = false;
        return null;
    }

    /**
     * Creates a shell account used to open an account.
     * @param commandArg The parsed command taken from the input.
     * @return the shell Account object.
     */
    private Account makeAccount(String[] commandArg) {
        String[] parsedBday = commandArg[BDAY_IND].split("/");
        Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                madeAccount = true;
                if (checkDate(birthday, "C")) {
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    return new Checking(newProfile, Double.parseDouble(commandArg[BALANCE_IND]));
                }
            } case "CC" -> {
                madeAccount = true;
                if (checkDate(birthday, "CC")) {
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    if (checkCampus(commandArg)) { // if it's a valid campus
                        return new CollegeChecking(newProfile, Double.parseDouble(commandArg[BALANCE_IND]), findCampus(Integer.parseInt(commandArg[STATUS_IND])));
                    }
                }
            } case "S" -> {
                madeAccount = true;
                if (checkDate(birthday, "S")) {
                    Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                    return new Savings(newProfile, Double.parseDouble(commandArg[BALANCE_IND]), !commandArg[STATUS_IND].equals("0"));
                }
            } case "MM" -> {
                if (Double.parseDouble(commandArg[BALANCE_IND]) < MM_MIN) {
                    System.out.println("Minimum of $2000 to open a Money Market account.");
                } else {
                    madeAccount = true;
                    if (checkDate(birthday, "MM")){
                        Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                        return new MoneyMarket(newProfile, Double.parseDouble(commandArg[BALANCE_IND]), true, 0);
                    }
                }
            }
        } madeAccount = false;
        return null;
    }

    /**
     * Creates a shell account used to deposit to an existing account.
     * @param commandArg The parsed command taken from the input.
     * @return the shell account object.
     */
    private Account makeDepAccount(String[] commandArg) {
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                return new Checking(newProfile, Double.parseDouble(commandArg[5]));

            }
            case "CC" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                return new CollegeChecking(newProfile, Double.parseDouble(commandArg[5]), Campus.CAMDEN);

            }
            case "S" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                return new Savings(newProfile, Double.parseDouble(commandArg[5]), true);

            }
            case "MM" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                return new MoneyMarket(newProfile, Double.parseDouble(commandArg[5]), true, 0);

            }
        } madeAccount = false;
        return null;
    }

    /**
     * Deposits balance into an account given it meets requirements.
     * @param command The parsed command read from the input.
     * @param database the Account database that holds all of the accounts.
     */
    private void depositAccount(String[] command, AccountDatabase database){
        if (checkDepositProperBalance(command[5])) {
            Account temp = makeDepAccount(command);
            if (!database.depositNotFound(temp)) {
                database.deposit(temp);
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " Deposit - balance updated.");
            } else {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " is not in the database.");
            }
        }
    }

    /**
     * Creates a shell account used to deposit to an existing account.
     * @param commandArg The parsed command taken from the input.
     * @param db The Account database that holds all of the accounts.
     * @return the shell Account object.
     */
    private Account makeWithdrawAccount(String[] commandArg, AccountDatabase db) {
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                return new Checking(newProfile, Double.parseDouble(commandArg[BALANCE_IND]));
            }
            case "CC" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                return new CollegeChecking(newProfile, Double.parseDouble(commandArg[BALANCE_IND]), Campus.CAMDEN);
            }
            case "S" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                return new Savings(newProfile, Double.parseDouble(commandArg[BALANCE_IND]), true);
            }
            case "MM" -> {
                madeDepAccount = true;
                String[] parsedBday = commandArg[BDAY_IND].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[YR]), Integer.parseInt(parsedBday[MONTH]), Integer.parseInt(parsedBday[DAY]));
                Profile newProfile = new Profile(commandArg[FNAME_IND], commandArg[LNAME_IND], birthday);
                Account moneyMarket = new MoneyMarket(newProfile, Double.parseDouble(commandArg[BALANCE_IND]), true, 0);
                db.updateWithdraws(moneyMarket);
                return moneyMarket;
            }
        } madeAccount = false;
        return null;
    }

    /**
     * Withdraw an amount from an account, given that it is possible.
     * @param command The parsed command taken from the input.
     * @param database the Account database that holds all of the accounts.
     */
    private void withdrawAccount(String [] command, AccountDatabase database){
        if (checkWithdrawProperBalance(command[5])) {
            Account temp = makeWithdrawAccount(command, database);
            if (database.contains(temp)) {
                if (database.withdraw(temp)) {
                    System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                            " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " Withdraw - balance updated.");
                } else {
                    System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                            " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " Withdraw - insufficient fund.");
                }
            } else {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[DATE_IND].toUpperCase() + ")" + " is not in the database.");

            }


        }
    }


    /**
     * Try-catch exception for an invalid command.
     * @param commandArg The argument in the parsed command that represents the account action.
     * @return true if the command is valid, false otherwise.
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
     * Try-catch IllegalArgumentException for an invalid account type.
     * @param commandArgAcctType The argument in the parsed command that represents the account type.
     * @return true if the account type if valid, false otherwise.
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

    /**
     * Try-catch NumberFormatException for an invalid amount.
     * @param balance The argument in the parsed command that represents the balance amount.
     * @return true if valid balance, false otherwise.
     */
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
     * Try-catch NumberFormatException for an invalid deposit amount.
     * @param balance The argument in the parsed command that represents the deposit amount.
     * @return true if a valid deposit amount, false otherwise.
     */
    private boolean checkDepositProperBalance(String balance) {
        try {
            Double testing = Double.parseDouble(balance);
            if (testing <= INVALID_DEP) {
                System.out.println("Deposit - amount cannot be 0 or negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Not a valid amount.");
            return false;
        }
        return true;
    }

    /**
     * Try-catch NumberFormatExecption for an invalid withdraw amount.
     * @param balance The argument in the parsed command that represents the withdraw amount.
     * @return true if a valid withdraw amount, false otherwise.
     */
    private boolean checkWithdrawProperBalance(String balance) {
        try {
            Double testing = Double.parseDouble(balance);
            if (testing <= INVALID_DEP) {
                System.out.println("Withdraw - amount cannot be 0 or negative.");
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
     * @param command The parsed command array.
     * @return true if the number of arguments present, is the number of arguments expected, false otherwise.
     */
    private boolean checkNoArgs(String[] command){
        String cmdType = command[INVALID_DEP];
        String acctType = null;
        int numArgs = command.length;
        try {
            if (cmdType.equals("O")){
                if (numArgs < OPEN_ARGS_C_MM) {
                    throw new InputMismatchException("Missing data for opening an account.");
                } else {
                    acctType = command[INTIAL_CMD];
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

    /**
     * Determines if an entered campus code is valid.
     * @param commandArg The parsed command array.
     * @return true if the campus is valid, false otherwise.
     */
    private boolean checkCampus(String [] commandArg){
        int campusCode = Integer.parseInt(commandArg[OPEN_ARGS_C_MM]);
        if (campusCode != INVALID_DEP && campusCode != PRINT_ARGS && campusCode != FNAME_IND){
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

    /**
     * Determines the Campus based on a given code.
     * @param campusCode the campus code.
     * @return the Campus corresponding to the campus code.
     */
    private Campus findCampus(int campusCode){
        Campus campus = Campus.NEWARK;
        for (Campus check: Campus.values()) {
            if (check.getCode() == campusCode) {
                campus = check;
            }
        }

        return campus;
    }

    /**
     * Determines if a date is valid.
     * @param bday The Date associated with the account.
     * @param accountType The type of bank account.
     * @return true if the date is valid, false otherwise.
     */
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
            if (temp.age() < MIN_AGE ){
                System.out.println("DOB invalid: " + bday + " under 16.");
                return false;
            } else if (temp.age() > MAX_AGE){
                System.out.println("DOB invalid: " + bday + " over 24.");
                return false;
            } else if (temp.age() == MAX_AGE){
                if(bday.getDay() < todaysDay){
                    System.out.println("DOB invalid: " + bday + " over 24.");
                    return false;
                }
            }
        } else if (!accountType.equals("CC")){
            if (temp.age() < MIN_AGE ){
                System.out.println("DOB invalid: " + bday + " under 16.");
                return false;
            }
        }
        return true;
    }



}
