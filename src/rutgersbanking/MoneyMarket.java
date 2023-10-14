package rutgersbanking;

public class MoneyMarket extends Savings {
    private int withdrawal; //number of withdrawals

    private static final double INTEREST_RATE = 0.045;
    private static final int FEE = 25;
    private static final int MIN_AGE = 16;
    private static boolean applyFee = true; // Boolean to check if monthly fee is waived
    private static final double  LOYAL_INTEREST_RATE = 0.0475; // Interest rate for loyal customers
    private static final int EXCEED_WITHDRAWAL_FEE = 10; // If the withdrawal > 3, then $10 fee is deducted.

    /**
     * Constructor to initialize the instance variable.
     * @param withdrawal Number of withdrawals.
     */
    public MoneyMarket(Profile holder, double balance, boolean isLoyal, int withdrawal) {
        super(holder, balance, true);
        this.withdrawal = withdrawal;
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

    /**
     * Determine if the user is old enough to have an account.
     * @return true if the profile holder is above 16, false otherwise.
     */
    @Override
    public boolean checkAge() {
        if (holder.age() >= MIN_AGE){
            return true;
        }
        return false;
    }


    public boolean balanceIsValid(boolean openingAccount) {
        if (openingAccount) { // Checking if this is the first time an account is being opened
            if (balance >= 2000) {
                return true;
            } else {
                return false;
            }
        } else if (balance <= 0) {
            return false;
        }

        return true;
    }

    private void checkWithdrawal() {
        if (withdrawal > 3){
            balance -= EXCEED_WITHDRAWAL_FEE;
        }
    }

    public int compareTo(Savings compareMoneyMarket){
        return -1;
    }


}
