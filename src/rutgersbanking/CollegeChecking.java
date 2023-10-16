package rutgersbanking;
import java.text.DecimalFormat;

/**
 * Defines a College Checking account for a user based on their information, balance, and college campus.
 * @author Deshna Doshi, Haejin Song
 */

public class CollegeChecking extends Checking {
    private Campus campus;

    private static final double INTEREST_RATE = 0.01;
    private static final int FEE = 0; // No monthly fee for CollegeChecking
    private static final int MIN_AGE = 16;
    private static final int MAX_AGE = 24;

    /**
     * Constructor to initialize the instance variable.
     * @param campus the Campus associated with the College Checking account.
     */
    public CollegeChecking(Profile holder, double balance, Campus campus) {
        super(holder, balance);
        this.campus = campus;
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
     * Provides the campus code associated with the account.
     * @return the campus code (0, 1, 2, representing NB, Newark, Camden respectively).
     */
    public int getCampusCode() {
        return campus.getCode();
    }

    /**
     * Determines if the account holder is at least 16 and under 24.
     * @return true if the holder is at least 16 and under 24, false otherwise.
     */
    @Override
    public boolean checkAge() {
        if (holder.age() >= MIN_AGE && holder.age() <= MAX_AGE){
            return true;
        }
        return false;
    }

    /**
     * Determines if the balance amount if valid.
     * Same implementation as parent class, Checking.
     * @return true if the balance is more than 0, false otherwise.
     */
    @Override
    public boolean balanceIsValid() {
        return super.balanceIsValid();
    }


    /**
     * Updates the balance with the interest rate and fees applied.
     */
    @Override
    public void updateBalance() {
        super.updateBalance();
    }

    /**
     * Calculates the monthly interest that applies to the account.
     * @return the monthly interest.
     */
    @Override
    public double calcInterest() {
        return super.calcInterest();
    }

    /**
     * Calculates the fee that applies to the account. (For a CollegeChecking, the fee is 0)
     * @return 0, the fee of a CollegeChecking account.
     */
    @Override
    public int calcFee() {
        return 0;
    }

    /**
     * Displays the account information.
     * @return a String of the account information.
     */
    @Override
    public String toString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String balanceFormat = currencyFormat.format(balance);

        return "College Checking::" + holder.toString() + "::Balance " + balanceFormat + "::" + campus.toString();
    }

    /**
     * Updates the balance with the fees and monthly interest, displays the account information
     * @return a String of the account information, with fees and interest included.
     */
    @Override
    public String netBalanceToString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String balanceFormat = currencyFormat.format(balance);
        String feeFormat = currencyFormat.format(calcFee());
        String interestFormat = currencyFormat.format(calcInterest());

        return "College Checking::" + holder.toString() + "::Balance " + balanceFormat + "::" + campus.toString()
                + "::fee " + feeFormat + "::monthly interest " + interestFormat;
    }

    @Override
    public boolean equals(Object compareCollegeChecking){
        // if (compareChecking == null) return false;
        if (getClass() != compareCollegeChecking.getClass()){
            return false;
        }

        Account collchecking = (Account) compareCollegeChecking; // type cast to use in equals

        boolean fnameMatch = false;
        boolean lnameMatch = false;
        boolean dobMatch = false;

        if (collchecking.getHolder().getFname().equals(this.holder.getFname())){
            fnameMatch = true;
        }
        if (collchecking.getHolder().getLname().equals(this.holder.getLname())){
            lnameMatch = true;
        }
        if (collchecking.getHolder().getDOB().compareTo(this.holder.getDOB()) == 0){
            dobMatch = true;
        }

        return (fnameMatch && lnameMatch && dobMatch);
    }

}
