package biyDaalt1;

public class Major {
    private String majorCode; // код
    private String majorName; // нэр

    public Major(String majorCode, String majorName) {
        this.majorCode = majorCode;
        this.majorName = majorName;
    }

    public String getMajorCode() { 
    	return majorCode; 
    }
    public String getMajorName() { 
    	return majorName; 
    }

    @Override
    public String toString() {
        return majorCode + " - " + majorName;
    }
}

