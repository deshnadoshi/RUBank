package rutgersbanking;

public class AccountDatabase {
    private static final int GROWTH_CAPACITY = 4;
    private Account [] accounts; // list of various types of accounts
    private int numAcct = 0; // number of accounts in the array

    private static final int NOT_FOUND = -1;

    /**
     * Constructor for the AccountDatabase class.
     * @param accounts
     * @param numAcct
     */
    public AccountDatabase(Account[] accounts, int numAcct) {
        this.accounts = new Account[0];
        this.numAcct = 0;
    }

    /**
     *
     * @param account
     * @return
     */
    private int find(Account account) {
        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].equals(account)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Increases the size of the accounts array by 4.
     */
    private void grow() {
        Account [] growAccountsArray = new Account[accounts.length + GROWTH_CAPACITY];
        for (int i = 0; i < accounts.length; i++) {
            growAccountsArray[i] = accounts[i];
        }
        accounts = growAccountsArray;
    } //increase the capacity by 4

    /**
     * Check if an account already exists in the list.
     * @param account Account to be compared
     * @return true if account is already in the database, false otherwise
     */
    public boolean contains(Account account) {
        return find(account) != NOT_FOUND;
    } //overload if necessary

    /**
     * Add an account to the array, if it doesn't already exist
     * @param account Account to be opened
     * @return true if the account is added, false if it is not added.
     */
    public boolean open(Account account) {
        if (!this.contains(account)) {
            if (this.numAcct >= this.accounts.length) {
                this.grow();
            }
            this.accounts[this.numAcct] = account;
            this.numAcct += 1;
            return true;
        }
        return false;
    } //add a new account

    /**
     *
     * @param account
     * @return
     */
    public boolean close(Account account) {
        int removedAccountIndex = find(account);
        if (removedAccountIndex == NOT_FOUND) return false;

        for (int i = removedAccountIndex; i < this.numAcct - 1; i++) {
            this.accounts[i] = this.accounts[i + 1];
        }

        this.accounts[this.numAcct] = null;

        this.numAcct -= 1;
        return true;
        //remove the given account
    }

    /**
     *
     * @param account
     * @return
     */
    public boolean withdraw(Account account) {
        int withdrawFromAccount = find(account);
        if (withdrawFromAccount == NOT_FOUND) return false; // if account doesn't exist you can't withdraw
        // account.balance is the amount to withdraw
        // need check if account.balance is > the real account's current balance
        if (account.getBalance() <= accounts[withdrawFromAccount].getBalance()){
            accounts[withdrawFromAccount].balance -= account.getBalance();
            // reduce the withdrawn amount from the real account's current balance
            return true;
        }

        // false if insufficient fund
        return false;
    }

    /**
     *
     * @param account
     */
    public void deposit(Account account) {
        int depositToAccount = find(account);
        // account.getBalance() contains the amount to deposit
        // Will need to create a "shell" account to hold just the deposit amount, to populate it into the actual account
        accounts[depositToAccount].balance += account.getBalance();

    }
    public void printSorted() {

    } //sort by account type and profile

    public void printFeesAndInterests() {

    } //calculate interests/fees

    public void printUpdatedBalances() {

    } //apply the interests/fees (1 month passes)

    /**
     Implementation of Quicksort for print methods.
     @param unsortedArray the array that is being sorted
     @param low the first index of the part of the array being sorted
     @param high the last index of the part of the array being sorted
     */
    public void quickSort(Account[] unsortedArray, int low, int high) {
        if (low >= high || low < 0) {
            return;
        }
        int pivot = partition(unsortedArray, low, high);
        quickSort(unsortedArray, low, pivot - 1);
        quickSort(unsortedArray, pivot + 1, high);
    }

    private int partition(Account[] unsortedArray, int low, int high) {
        Account pivot = unsortedArray[high];
        int temp_pivot = low - 1;
        for (int i = low; i < high; i++) {
            if (unsortedArray[i].getHolder().getLname().compareTo(pivot.getHolder().getLname()) < 0) {
                temp_pivot += 1;
                swap(i, temp_pivot, unsortedArray);
            } else if (unsortedArray[i].getHolder().getLname().compareTo(pivot.getHolder().getLname()) == 0 &&
                    (unsortedArray[i].getHolder().getFname().compareTo(pivot.getHolder().getFname()) < 0)) {
                temp_pivot += 1;
                swap(i, temp_pivot, unsortedArray);
            }
        }
        temp_pivot += 1;
        swap(temp_pivot, high, unsortedArray);
        return temp_pivot;
    }

    private void swap(int i, int j, Account[] A) {
        Account temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

}
