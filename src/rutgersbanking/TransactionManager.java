package rutgersbanking;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TransactionManager {
    private AccountDatabase accounts;
    private static final int INITIAL_ARR_SIZE = 4;
    private static final int OPEN_ARGS_CC_S = 7; // Arguments expected to open a CC/S account
    private static final int OPEN_ARGS_C_MM = 6; // Arguments expected to open a C/MM account
    private static final int CLOSE_ARGS = 5; // Arguments expected to close an account

    private static final int ACCT_TYPE_INDEX = 1; // Account type index in the parsed command


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
                        database.close(makeAccount(parsedCommand));
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
            if (database.open(temp)) {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[1].toUpperCase() + ")" + " opened.");
            } else {
                System.out.println(temp.getHolder().getFname() + " " + temp.getHolder().getLname() +
                        " " + temp.getHolder().getDOB() + "(" + command[1].toUpperCase() + ")" + " is already in the database.");
            }
        }
        if (command[ACCT_TYPE_INDEX].equals("S") || command[ACCT_TYPE_INDEX].equals("CC")) {
            return OPEN_ARGS_CC_S;
        } else {
            return OPEN_ARGS_C_MM;
        }
    }

    private Account makeAccount(String[] commandArg) {
        Account newAccount = null;
        switch (commandArg[ACCT_TYPE_INDEX]) {
            case "C" -> {
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                newAccount = new Checking(newProfile, Double.parseDouble(commandArg[5]));
            }
            case "CC" -> {
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                Campus campus = Campus.NEWARK;
                for (Campus check: Campus.values())
                    if (check.getCode() == Integer.parseInt(commandArg[6])) {
                        campus = check;
                    }
                newAccount = new CollegeChecking(newProfile, Double.parseDouble(commandArg[5]), campus);
            }
            case "S" -> {
                String[] parsedBday = commandArg[4].split("/");
                Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                newAccount = new Savings(newProfile, Double.parseDouble(commandArg[5]), Boolean.parseBoolean(commandArg[6]));
            }
            case "MM" -> {
                if (Double.parseDouble(commandArg[5]) < 2000) System.out.println("Minimum of $2000 to open a Money Market account.");
                else {
                    String[] parsedBday = commandArg[4].split("/");
                    Date birthday = new Date(Integer.parseInt(parsedBday[2]), Integer.parseInt(parsedBday[0]), Integer.parseInt(parsedBday[1]));
                    Profile newProfile = new Profile(commandArg[2], commandArg[3], birthday);
                    newAccount = new MoneyMarket(newProfile, Double.parseDouble(commandArg[5]), true, 0);
                }
            }
        } return newAccount;
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

    private boolean checkProperBirthday(Account account, String birthday, String[] command) {
        String[] birthdayParts = birthday.split("/");
        String acctType = command[ACCT_TYPE_INDEX];
        Date bday = new Date(Integer.parseInt(birthdayParts[2]), Integer.parseInt(birthdayParts[0]),
                Integer.parseInt(birthdayParts[1]));
        if (!bday.validCalendarDate()) {
            System.out.println("DOB invalid: " + birthday + " not a valid calendar date!");
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

}
