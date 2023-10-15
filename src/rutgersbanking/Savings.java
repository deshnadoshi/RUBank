package rutgersbanking;

public class Savings extends Account {
    protected boolean isLoyal;

    private static final double INTEREST_RATE = 0.04;
    private static final int FEE = 25;
    private static final int MIN_AGE = 16;
    private static final double  LOYAL_INTEREST_RATE = 0.0425; // Interest rate for loyal customers
    private static final int MONTHS = 12;
    private static final int EQUAL_COMPARATOR = 0;

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
    public int compareTo(Account o) {
        return 0;
    }
}
