package rutgersbanking;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TransactionManager {
    private AccountDatabase accounts;
    private static final int INITIAL_ARR_SIZE = 4;
    private static final int OPEN_ARGS_CC_S = 7; // Arguments expected to open a CC/S account
    private static final int OPEN_ARGS_C_MM = 6; // Arguments expected to open a C/MM account
    private static final int CLOSE_ARGS= 5; // Arguments expected to close an account

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
                    if (checkNoArgs(parsedCommand[counter], parsedCommand[ACCT_TYPE_INDEX], parsedCommand.length)){
                        // open account
                    }
                } else if (parsedCommand[counter].equals("C")) {
                    if (checkNoArgs(parsedCommand[counter], parsedCommand[ACCT_TYPE_INDEX], parsedCommand.length)){
                        // close account
                    }
                } else if (parsedCommand[counter].equals("D")) {
                    if (checkNoArgs(parsedCommand[counter], parsedCommand[ACCT_TYPE_INDEX], parsedCommand.length)){
                        // deposit into account
                    }
                } else if (parsedCommand[counter].equals("W")) {
                    if (checkNoArgs(parsedCommand[counter], parsedCommand[ACCT_TYPE_INDEX], parsedCommand.length)){
                        // withdraw from
                    }
                } else if (parsedCommand[counter].equals("P")) {
                    // don't need to check if it has the correct # of args, the input doesn't have any invalid print commands
                } else if (parsedCommand[counter].equals("PI")) {

                } else if (parsedCommand[counter].equals("UB")){

                } else if (parsedCommand[counter].equals("Q")){
                    return true;
                }

            }

            counter++; // delete this (just place holder to check if try-catch for invalid cmd was working)
        } return false;
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

    /**
     * Try-catch InputMismatchException for insufficient input arguments.
     * @param cmdType
     * @param acctType
     * @param numArgs
     * @return
     */
    private boolean checkNoArgs(String cmdType, String acctType, int numArgs){
        try {
            if (cmdType.equals("O")){
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
