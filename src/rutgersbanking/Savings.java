package rutgersbanking;

import java.text.DecimalFormat;

public class Savings extends Account {
    protected boolean isLoyal;

    private static final double INTEREST_RATE = 0.04;
    private static final int FEE = 25;
    private static final int MIN_AGE = 16;
    private static final double  LOYAL_INTEREST_RATE = 0.0425; // Interest rate for loyal customers
    private static final int MONTHS = 12;
    private static final int EQUAL_COMPARATOR = 0;
    private static final int NOT_EQUAL = -2;

    /**
     * Constructor to initialize the instance variable.
     * @param isLoyal Customer loyalty metric.
     */
    public Savings(Profile holder, double balance, boolean isLoyal) {
        super(holder, balance);
        this.isLoyal = isLoyal;
    }

    // MoneyMarket.java keeps on yelling at me whenever i don't have a default constructor for some reason so i've added one in
    // i'm not fully sure what it's supposed to be fore though
    public Savings(){

    }


    /**
     * Provides the monthly fee associated with the account.
     * @return the monthly fee.
     */
    public double monthlyFee(){
        return FEE;
    }

    /**
     * Provides the monthly interest rate associated with the account.
     * @return the monthly interest rate.
     */
    public double monthlyInterest(){
        return INTEREST_RATE;
    }

    @Override
    public boolean checkAge() {
        if(holder.age() >= MIN_AGE){
            return true;
        }
        return false;
    }


    @Override
    public boolean balanceIsValid() {
        if (balance <= 0){
            return false;
        }

        return true;
    }

    @Override
    public void updateBalance() {
        double monthlyInterest = 0;
        if (isLoyal) {
            monthlyInterest = (balance * LOYAL_INTEREST_RATE) / MONTHS;
            balance += monthlyInterest; // add the interest to the balance
        } else {
            monthlyInterest = (balance * INTEREST_RATE) / MONTHS;
            balance += monthlyInterest; // add the interest to the balance
        }
        if (checkApplyFee()){
            balance -= FEE;
        }
    }

    public boolean checkApplyFee(){
        if (balance >= 500){
            return false; // if the balance is more than 500 don't apply the fee
        }

        return true;
    }

    public double calcInterest() {
        return (balance * INTEREST_RATE) / MONTHS;
    }

    @Override
    public int calcFee() {
        if (checkApplyFee()){
            return FEE;
        }
        return 0;
    }


    /**
     *
     * @param compareSavings
     * @return
     */
    @Override
    public boolean equals(Object compareSavings){
        if (getClass() != compareSavings.getClass()){
            return false;
        }
        Account savings = (Savings) compareSavings; // type cast to use in equals

        boolean fnameMatch = false;
        boolean lnameMatch = false;
        boolean dobMatch = false;

        if (savings.getHolder().getFname().equals(holder.getFname())){
            fnameMatch = true;
        }
        if (savings.getHolder().getLname().equals(holder.getLname())){
            lnameMatch = true;
        }
        if (savings.getHolder().getDOB().compareTo(holder.getDOB()) == EQUAL_COMPARATOR){
            dobMatch = true;
        }

        return (fnameMatch && lnameMatch && dobMatch);
    }


    @Override
    public int compareTo(Account savings) {
        int profileCompare = savings.getHolder().compareTo(holder);
        boolean typeCompare = savings.getClass().equals(getClass());
        // makes sure that a MM doesn't get confused for a savings, if they have the same profile info

        if (profileCompare == 0 && typeCompare){
            return EQUAL_COMPARATOR;
        }

        return NOT_EQUAL; // if either profile is not the same or the type is not the same
    }

    @Override
    public String toString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String balanceFormat = currencyFormat.format(balance);

        if (isLoyal){
            return "Savings::" + holder.toString() + "::Balance " + balanceFormat + "::is loyal";
        }

        return "Savings::" + holder.toString() + "::Balance " + balanceFormat;
    }

    @Override
    public String netBalanceToString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        updateBalance();
        String balanceFormat = currencyFormat.format(balance);
        String feeFormat = currencyFormat.format(calcFee());
        String interestFormat = currencyFormat.format(calcInterest());

        if (isLoyal){
            return "Savings::" + holder.toString() + "::Balance " + balanceFormat + "::is loyal" + "::fee "
                    + feeFormat + "::monthly interest " + interestFormat;
        }
        return "College Checking::" + holder.toString() + "::Balance " + balanceFormat + "::fee "
                + feeFormat + "::monthly interest " + interestFormat;
    }

    // delete this later on (only for testing)
    public static void main (String [] args){
        Profile p = new Profile("h", "d", new Date(2003, 11, 4));
        Account mm = new MoneyMarket(p, 1111, true, 0);
        Account s = new Savings(p, 1123, true);
        Account s2 = new Savings (p, 1123, true);
        System.out.println(s.compareTo(mm)); // should not be equal, and they r not equal !!
        // can use compareto for find() function

        System.out.println(s.equals(mm)); // should be false because their classes don't match! yes ! false
        // use equals for deposit finding and withdraw finding

        System.out.println(s.equals(s2)); // should be true;  IT IS SLAY
    }
}
