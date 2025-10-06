package biyDaalt1;

public class Lessons {
    private Subject learned; // үзсэн хичээл
    private int score;       // шалгалтын оноо

    public Lessons(Subject s, int sc) {
        this.learned = s;
        this.score = sc;
    }

    public Subject getSubject() { 
    	return learned; 
    }
    public int getScore() { 
    	return score; 
    }

    public double getGPA() {
        if (score >= 95) return 4.0;
        if (score >= 90) return 3.7;
        if (score >= 87) return 3.3;
        if (score >= 83) return 3.0;
        if (score >= 80) return 2.7;
        if (score >= 77) return 2.3;
        if (score >= 73) return 2.0;
        if (score >= 70) return 1.7;
        if (score >= 65) return 1.3;
        if (score >= 60) return 1.0;
        return 0.0; // F
    }

    @Override
    public String toString() {
        return learned.getSubjectCode() + ":" + score + " (GPA=" + getGPA() + ")";
    }
}
