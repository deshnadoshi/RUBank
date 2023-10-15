package rutgersbanking;
import java.text.DecimalFormat;

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

    /**
     *
     * @return
     */
    @Override
    public boolean checkAge() {
        if (holder.age() >= MIN_AGE){
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean balanceIsValid() {
        return !(balance <= 0);
    }

    /**
     *
     */
    @Override
    public void updateBalance() {
        double monthlyInterest = (balance * INTEREST_RATE) / MONTHS;
        balance += monthlyInterest; // add the interest to the balance
        if (checkApplyFee()){
            balance -= FEE;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public double calcInterest() {
        return (balance * INTEREST_RATE) / MONTHS;
    }

    /**
     *
     * @return
     */
    @Override
    public int calcFee() {
        if (checkApplyFee()){
            return FEE;
        }
        return 0;
    }

    /**
     *
     * @return
     */
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

        if (getClass() != compareChecking.getClass()){
            return false;
        }

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

    /**
     *
     * @param checking the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Account checking) {
        return checking.getHolder().compareTo(holder);

    }

    @Override
    public String toString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String balanceFormat = currencyFormat.format(balance);

        return "Checking::" + holder.toString() + "::Balance " + balanceFormat;
    }

    /**
     *
     * @return
     */
    @Override
    public String netBalanceToString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        updateBalance();
        String balanceFormat = currencyFormat.format(balance);
        String feeFormat = currencyFormat.format(calcFee());
        String interestFormat = currencyFormat.format(calcInterest());

        return "Checking::" + holder.toString() + "::Balance " + balanceFormat + "::fee " + feeFormat +
                "::monthly interest " + interestFormat;
    }

    // delete this later on (only for testing)
    public static void main (String [] args){
        Profile p = new Profile("h", "d", new Date(2003, 11, 4));
        Account cc = new CollegeChecking(p, 1111, Campus.NEW_BRUNSWICK);
        Account c = new Checking(p, 1123);
        Account c2 = new Checking (p, 1123);
        System.out.println(c.compareTo(cc)); // should be equal! and it is!
        // use compareto for finding find() function and any cc or c that r same 2 same

        System.out.println(c.equals(cc)); // should be false because their classes don't match! yes ! false
        System.out.println(cc.equals(c));
        // use equals for deposit finding and withdraw finding


        System.out.println(c.equals(c2)); // should be true;  IT IS SLAY

        System.out.println(c.netBalanceToString());
        System.out.println(c.toString());
        System.out.println(cc.netBalanceToString());
        System.out.println(cc.toString());
    }
}
