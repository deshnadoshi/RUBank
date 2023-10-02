package rutgersbanking;

public class AccountDatabase {
    private static final int GROWTH_CAPACITY = 4;
    private Account [] accounts; //list of various types of accounts
    private int numAcct; //number of accounts in the array
    private int find(Account account) {} //search for an account in the array

    /**
     * Increases the size of the array by 4.
     */
    private void grow(){
        Account [] growAccountsArray = new Account[accounts.length + GROWTH_CAPACITY];
        for (int i = 0; i < accounts.length; i++){
            growAccountsArray[i] = accounts[i];
        }
        accounts = growAccountsArray;
    } //increase the capacity by 4
    public boolean contains(Account account){} //overload if necessary
    public boolean open(Account account){} //add a new account
    public boolean close(Account account){} //remove the given account
    public boolean withdraw(Account account){} //false if insufficient fund
    public void deposit(Account account){}
    public void printSorted(){} //sort by account type and profile
    public void printFeesAndInterests(){} //calculate interests/fees
    public void printUpdatedBalances(){} //apply the interests/fees
}
