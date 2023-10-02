package rutgersbanking;

public abstract class Account implements Comparable <Account>{
    protected Profile holder;
    protected double balance;
    public abstract double monthlyInterest();
    public abstract double monthlyFee();
}
