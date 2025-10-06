package biyDaalt1;

public class Major {
    private String majorCode; // код
    private String majorName; // нэр

    public Major(String code, String name) {
        this.majorCode = code;
        this.majorName = name;
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

