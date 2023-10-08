package rutgersbanking;

public class CollegeChecking extends Checking {
    private Campus campus;
    private static final double INTEREST_RATE = 0.01;
    private static final int FEE = 12;

    /**
     * Constructor to initialize the instance variable.
     * @param campus Campus associated with the College Checking account.
     */
    public CollegeChecking(Campus campus){
        this.campus = campus;
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
     * Provides the campus code associated with the account.
     * @return the campus code (0, 1, 2, representing NB, Newark, Camden respectively).
     */
    public int getCampusCode(){
        return campus.getCode();
    }

    public int compareTo(Checking compareCollegeChecking){
        return -1;
    }
}
