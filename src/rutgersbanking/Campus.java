package rutgersbanking;

public enum Campus {
    NewBrunswick(0),
    Newark(1),
    Camden(2);

    private final int CODE;

    Campus(int code) {
        this.CODE = code;
    }

    public int getCode() {
        return CODE;
    }

}
