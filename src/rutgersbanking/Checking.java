package rutgersbanking;

public class Checking extends Account {
    private static final double INTEREST_RATE = 0.01;
    private static final int FEE = 12;
    private static final int MIN_AGE = 16;
    private static final int MONTHS = 12;
    private static final int EQUAL_COMPARATOR = 0;

    public Checking(Profile holder, double balance) {
        super(holder, balance);
    }

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
        if (holder.age() >= MIN_AGE){
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
        double monthlyInterest = (balance * INTEREST_RATE) / MONTHS;
        balance += monthlyInterest; // add the interest to the balance
        if (checkApplyFee()){
            balance -= FEE;
        }
    }

    @Override
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


    public boolean checkApplyFee(){
        return !(balance >= 1000); // if the balance is more than 500 don't apply the fee
    }


    /**
     *
     * @param compareChecking
     * @return
     */
    @Override
    public boolean equals(Object compareChecking){

        Account checking = (Account) compareChecking; // type cast to use in equals

        boolean fnameMatch = false;
        boolean lnameMatch = false;
        boolean dobMatch = false;

        if (checking.getHolder().getFname().equals(holder.getFname())){
            fnameMatch = true;
        }
        if (checking.getHolder().getLname().equals(holder.getLname())){
            lnameMatch = true;
        }
        if (checking.getHolder().getDOB().compareTo(holder.getDOB()) == EQUAL_COMPARATOR){
            dobMatch = true;
        }

        return (fnameMatch && lnameMatch && dobMatch);
    }

    @Override
    public int compareTo(Account o) {
        return 0;
    }
}
