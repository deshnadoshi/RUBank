package rutgersbanking;

public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    private final int NOT_EQUAL = -2;
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

    public String getFname(){
        return fname;
    }

    public String getLname(){
        return lname;
    }

    public Date getDOB(){
        return dob;
    }

}
