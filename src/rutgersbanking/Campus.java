package rutgersbanking;

public enum Campus {
    NEW_BRUNSWICK(0),
    NEWARK(1),
    CAMDEN(2);

    private final int CODE;

    Campus(int code) {
        this.CODE = code;
    }

    public int getCode() {
        return CODE;
    }


}
