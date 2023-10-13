package rutgersbanking;

public class Savings extends Account {
    protected boolean isLoyal;

    private static final double INTEREST_RATE = 0.04;
    private static final int FEE = 25;
    private static final int MIN_AGE = 16;
    private static boolean applyFee = true; // Boolean to check if monthly fee is waived
    private static final double  LOYAL_INTEREST_RATE = 0.0425; // Interest rate for loyal customers



    /**
     * Constructor to initialize the instance variable.
     * @param isLoyal Customer loyalty metric.
     */
    public Savings(boolean isLoyal){
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

//    @Override
//    public boolean checkBalance() {
//        if (balance >= 500){
//            applyFee = false; // Monthly fee waived
//        }
//
//        if (balance <= 0){
//            return false;
//        }
//
//        return true;
//    }


    @Override
    public boolean balanceIsValid() {
        if (balance <= 0){
            return false;
        }

        return true;
    }

    public int compareTo(Account compareSavings){
        return -1;
    }
}
