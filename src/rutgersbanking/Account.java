package rutgersbanking;

public abstract class Account implements Comparable <Account>{
    protected Profile holder;
    protected double balance;

    /**
     * Constructor to initialize the instance variables.
     * @param holder Profile information of the account holder.
     * @param balance Balance in the account.
     */
    public Account(Profile holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }

    public Account() {

    }

    /**
     * Abstract method to be implemented in subclasses; provides the monthly fee associated with the account.
     */
    public abstract double monthlyFee();

    /**
     * Abstract method to be implemented in subclasses; provides the monthly interest rate associated with the account.
     */
    public abstract double monthlyInterest();

    public abstract boolean checkAge();

    public abstract boolean balanceIsValid();

    // might need equals() as well (just a note)
    public abstract void updateBalance();

    public abstract double calcInterest();

    public abstract int calcFee();

    public abstract String netBalanceToString();
    @Override
    public abstract String toString();

    public double getBalance() {
        return balance;
    }

    public Profile getHolder() {
        return holder;
    }

}
