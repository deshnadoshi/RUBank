package rutgersbanking;

public class MoneyMarket extends Savings {
    private int withdrawal; //number of withdrawals
    private static final double INTEREST_RATE = 0.45;
    private static final int FEE = 25;

    /**
     * Constructor to initialize the instance variable.
     * @param withdrawal Number of withdrawals.
     */
    public MoneyMarket(int withdrawal){
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
    public int compareTo(Savings compareMoneyMarket){
        return -1;
    }


}
