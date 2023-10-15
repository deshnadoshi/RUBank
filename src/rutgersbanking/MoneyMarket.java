package rutgersbanking;

import java.text.DecimalFormat;

public class MoneyMarket extends Savings {
    private int withdrawal; //number of withdrawals

    private static final double INTEREST_RATE = 0.045;
    private static final int FEE = 25;
    private static final int MIN_AGE = 16;
    private static final double  LOYAL_INTEREST_RATE = 0.0475; // Interest rate for loyal customers
    private static final int EXCEED_WITHDRAWAL_FEE = 10; // If the withdrawal > 3, then $10 fee is deducted.
    private static final int EQUAL_COMPARATOR = 0;
    private static final int NOT_EQUAL = -2;

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
        if (getClass() != compareMoneyMarket.getClass()){
            return false;
        }

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

    @Override
    public int compareTo(Account savings) {
        int profileCompare = savings.getHolder().compareTo(holder);
        boolean typeCompare = savings.getClass().equals(getClass());
        // makes sure that a MM doesn't get confused for a savings, if they have the same profile info

        if (profileCompare == 0 && typeCompare){
            return EQUAL_COMPARATOR;
        }

        return NOT_EQUAL; // if either profile is not the same or the type is not the same
    }

    @Override
    public String toString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        String balanceFormat = currencyFormat.format(balance);

        if (isLoyal){
            return "Money Market::Savings::" + holder.toString() + "::Balance " + balanceFormat + "::is loyal::withdrawal: " + withdrawal;
        }

        return "Money Market::Savings::" + holder.toString() + "::Balance " + balanceFormat + "::withdrawal: " + withdrawal;
    }

    @Override
    public String netBalanceToString(){
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        updateBalance();
        String balanceFormat = currencyFormat.format(balance);
        String feeFormat = currencyFormat.format(calcFee());
        String interestFormat = currencyFormat.format(calcInterest());

        if (isLoyal){
            return "Money Market::Savings::" + holder.toString() + "::Balance " + balanceFormat + "::is loyal::withdrawal: " + withdrawal
                    + "::fee " + feeFormat + "::monthly interest " + interestFormat;
        }
        return "Money Market::Savings::" + holder.toString() + "::Balance " + balanceFormat + "::is loyal::withdrawal: " + withdrawal
                + "::fee " + feeFormat + "::monthly interest " + interestFormat;
    }

    // delete this later, only for testing
    public static void main (String [] args){
        Profile p = new Profile("name1", "name2", new Date(2003, 11, 4));
        Account mm = new MoneyMarket(p, 2001, true, 0);
        Account mm2 = new MoneyMarket(p, 1111, true, 0);
        Account s = new Savings(p, 1123, true);
        Account s2 = new Savings (p, 1123, true);
        System.out.println(s.compareTo(mm)); // should not be equal, and they r not equal !!
        // can use compareto for find() function

        System.out.println(s.equals(mm)); // should be false because their classes don't match! yes ! false
        // use equals for deposit finding and withdraw finding

        System.out.println(s.equals(s2)); // should be true;  IT IS SLAY
        System.out.println(mm.compareTo(mm2)); // should be 0: it is
        System.out.println(mm.equals(mm2)); // should be true: it is
        System.out.println(mm.toString());
        System.out.println(mm.netBalanceToString());
    }

}
