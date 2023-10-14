package rutgersbanking;

public class Checking extends Account{
    private static final double INTEREST_RATE = 0.01;
    private static final int FEE = 12;
    private static final int MIN_AGE = 16;
    private static boolean applyFee = true; // Boolean to check if monthly fee is waived

    /**
     * Provides the monthly fee associated with the account.
     * @return the monthly fee.
     */
    public double monthlyFee() {
        return FEE;
    }

    /**
     * Provides the monthly interest rate associated with the account.
     * @return the monthly interest rate.
     */
    public double monthlyInterest() {
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
    public int compareTo(Account compareChecking) {
        return -1;
    }

}
