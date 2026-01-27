package biyDaalt1;

public class Subject {
    private String subjectCode; // код
    private String subjectName; // нэр
    private float credit;       // кредит

    public Subject(String code, String name, float credit) {
        this.subjectCode = code;
        this.subjectName = name;
        this.credit = credit;
    }

    public String getSubjectCode() { 
    	return subjectCode; 
    }
    public String getSubjectName() { 
    	return subjectName; 
    }
    public float getCredit() { 
    	return credit; 
    }

    @Override
    public String toString() {
        return subjectCode + " - " + subjectName + " (" + credit + ")";
    }
}
