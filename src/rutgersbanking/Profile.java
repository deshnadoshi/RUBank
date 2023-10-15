package rutgersbanking;
import java.util.Calendar;

public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    private final int NOT_EQUAL = -2; // for the compareTo method; -2 output means profiles are not equal
    public Profile(String fname, String lname, Date dob){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    public boolean isValidDOB(){
       return dob.isValid();
    }

    @Override
    public int compareTo(Profile compareProfile){
        int compareResult = 0;

        int fNameCompareResult = fname.compareTo(compareProfile.getFname());
        int lNameCompareResult = lname.compareTo(compareProfile.getLname());
        int dobCompareResult = dob.compareTo(compareProfile.getDOB());

        if (fNameCompareResult == 0 && lNameCompareResult == 0 && dobCompareResult == 0){
            return compareResult;
        }

        return NOT_EQUAL;
    }

    /**
     * Determine if the profile holder is within the required age to open/hold an account
     * @return age of the account holder
     */
    public int age(){
        Calendar currentDate = Calendar.getInstance();
        int age = currentDate.get(Calendar.YEAR) - dob.getYear();

        // Check if the birthday has already passed this year
        if (currentDate.get(Calendar.MONTH) < dob.getMonth() || (currentDate.get(Calendar.MONTH) == dob.getMonth() &&
                currentDate.get(Calendar.DAY_OF_MONTH) < dob.getDay())) {
            age--;
        }

        return age;
    }

    /**
     *
     * @return
     */
    public String toString(){
        return fname + " " + lname + " " + dob.toString();
    }

    /**
     * 
     * @return
     */
    public String getFname(){
        return fname;
    }

    /**
     *
     * @return
     */
    public String getLname(){
        return lname;
    }

    /**
     *
     * @return
     */
    public Date getDOB(){
        return dob;
    }

}
