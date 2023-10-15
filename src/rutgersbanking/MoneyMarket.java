package rutgersbanking;

public class MoneyMarket extends Savings {
    private int withdrawal; //number of withdrawals

    private static final double INTEREST_RATE = 0.045;
    private static final int FEE = 25;
    private static final int MIN_AGE = 16;
    private static final double  LOYAL_INTEREST_RATE = 0.0475; // Interest rate for loyal customers
    private static final int EXCEED_WITHDRAWAL_FEE = 10; // If the withdrawal > 3, then $10 fee is deducted.
    private static final int EQUAL_COMPARATOR = 0;

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
        return holder.age() >= MIN_AGE;
    }


    public boolean balanceIsValid(boolean openingAccount) {
        if (openingAccount) { // Checking if this is the first time an account is being opened
            return balance >= 2000;
        } else return !(balance <= 0);
    }

    private void checkWithdrawal() {
        if (withdrawal > 3){
            balance -= EXCEED_WITHDRAWAL_FEE;
        }
    }

    @Override
    public double calcInterest() {
        return super.calcInterest();
    }

    @Override
    public int calcFee() {
        return super.calcFee();
    }

    @Override
    public boolean checkApplyFee(){
        return !(balance >= 2000) || withdrawal > 3; // if the balance is more than 2000 and withdrawal <= 3 don't apply the fee
    }

    public boolean checkLoyalty(){
        if (balance >= 2000){
            isLoyal = true;
            return true;
        } else {
            isLoyal = false;
        }
        return false;
    }


    @Override
    public boolean equals(Object compareMoneyMarket){

        Account moneymarket = (Savings) compareMoneyMarket; // type cast to use in equals

        boolean fnameMatch = false;
        boolean lnameMatch = false;
        boolean dobMatch = false;

        if (moneymarket.getHolder().getFname().equals(holder.getFname())){
            fnameMatch = true;
        }
        if (moneymarket.getHolder().getLname().equals(holder.getLname())){
            lnameMatch = true;
        }
        if (moneymarket.getHolder().getDOB().compareTo(holder.getDOB()) == EQUAL_COMPARATOR){
            dobMatch = true;
        }

        return (fnameMatch && lnameMatch && dobMatch);
    }



}
