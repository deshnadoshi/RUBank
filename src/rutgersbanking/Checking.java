package rutgersbanking;

public class Checking extends Account{
    private static final double INTEREST_RATE = 0.01;
    private static final int FEE = 12;

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
    public int compareTo(Account compareChecking) {
        return -1;
    }

}
