package rutgersbanking;

public class AccountDatabase {
    private static final int GROWTH_CAPACITY = 4;
    private Account [] accounts; //list of various types of accounts
    private int numAcct; //number of accounts in the array
    private int find(Account account) {
        return 0;
    } //search for an account in the array

    /**
     * Increases the size of the accounts array by 4.
     */
    private void grow(){
        Account [] growAccountsArray = new Account[accounts.length + GROWTH_CAPACITY];
        for (int i = 0; i < accounts.length; i++){
            growAccountsArray[i] = accounts[i];
        }
        accounts = growAccountsArray;
    } //increase the capacity by 4
    public boolean contains(Account account){
        return false;
    } //overload if necessary
    public boolean open(Account account){
        return false;
    } //add a new account
    public boolean close(Account account){
        return false;
    } //remove the given account
    public boolean withdraw(Account account){
        return false;
    } //false if insufficient fund
    public void deposit(Account account){}
    public void printSorted(){} //sort by account type and profile
    public void printFeesAndInterests(){} //calculate interests/fees
    public void printUpdatedBalances(){} //apply the interests/fees
}
