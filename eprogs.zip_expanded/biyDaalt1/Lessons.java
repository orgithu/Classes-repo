package biyDaalt1;

public class Lessons {
    private Subject learned; // үзсэн хичээл
    private int score;       // шалгалтын оноо

    public Lessons(Subject learned, int score) {
        this.learned = learned;
        this.score = score;
    }

    public Subject getLearned() { 
    	return learned; 
    }
    public int getScore() { 
    	return score; 
    }

    public float getGPAValue() {
        if (score >= 95) return 4.0f;
        else if (score >= 90) return 3.7f;
        else if (score >= 87) return 3.3f;
        else if (score >= 83) return 3.0f;
        else if (score >= 80) return 2.7f;
        else if (score >= 77) return 2.3f;
        else if (score >= 73) return 2.0f;
        else if (score >= 70) return 1.7f;
        else if (score >= 65) return 1.3f;
        else if (score >= 60) return 1.0f;
        else return 0.0f; // F
    }
    public String getGrade() {
    	if (score >= 95) return "A+";
        else if (score >= 90) return "A";
        else if (score >= 87) return "B+";
        else if (score >= 83) return "B";
        else if (score >= 80) return "B-";
        else if (score >= 77) return "C+";
        else if (score >= 73) return "C";
        else if (score >= 70) return "C-";
        else if (score >= 65) return "D";
        else if (score >= 60) return "D-";
        else return "F"; // F
    }

    @Override
    public String toString() {
        return learned.getSubjectCode() + ":" + score + " (GPA=" + getGPAValue() + ")";
    }
}
