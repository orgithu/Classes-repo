package biyDaalt1;
import dataStructures.Chain;

public class Student {
    private String studentCode; // код
    private float GPA;          // голч дүн
    public Chain lessons;      // үзсэн хичээлүүд (жагсаалт)

    public Student(String code) {
        this.studentCode = code;
        this.lessons = new Chain(); // эсвэл ArrayLinearList
    }

    public void addLesson(Lessons l) {
        lessons.add(lessons.size(), l);
    }

    public String getStudentCode() { return studentCode; }

    public float getGPA() {
        if (lessons.size() == 0) return 0;
        double sum = 0;
        for (int i = 0; i < lessons.size(); i++) {
            Lessons l = (Lessons) lessons.get(i);
            sum += l.getGPA();
        }
        return (float)(sum / lessons.size());
    }

    public int countF() {
        int c = 0;
        for (int i = 0; i < lessons.size(); i++) {
            Lessons l = (Lessons) lessons.get(i);
            if (l.getGPA() == 0.0) c++;
        }
        return c;
    }

    @Override
    public String toString() {
        return studentCode + " AvgGPA=" + getGPA();
    }
}