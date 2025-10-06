package biyDaalt1;
import java.io.*;
import java.util.*;
import dataStructures.ArrayLinearList;

public class Registration {
    public ArrayLinearList studentList = new ArrayLinearList();
    public ArrayLinearList subjectList = new ArrayLinearList();
    public ArrayLinearList majorList   = new ArrayLinearList();

    public static void main(String[] args) {
        Registration reg = new Registration();
        try {
            reg.loadSubjects("Subjects.txt");
            reg.loadMajors("Professions.txt");
            reg.loadExams("Exams.txt");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        reg.showAllSubjects();
        reg.showAllMajors();
        reg.showOverallAverageGPA();
        reg.listStudentsWithMoreThan3F();
        reg.showGradesBySubject();
    }

    // --- Файл унших ---
    public void loadSubjects(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("/");
            Subject s = new Subject(p[0], p[1], Float.parseFloat(p[2]));
            subjectList.add(subjectList.size(), s);
        }
        br.close();
    }

    public void loadMajors(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("/");
            Major m = new Major(p[0], p[1]);
            majorList.add(majorList.size(), m);
        }
        br.close();
    }

    public void loadExams(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split("/");
            String stCode = p[0];
            String subjCode = p[1];
            int score = Integer.parseInt(p[2]);

            Subject subj = findSubject(subjCode);
            Student st = findOrCreateStudent(stCode);
            st.addLesson(new Lessons(subj, score));
        }
        br.close();
    }

    // --- Туслах функцүүд ---
    private Subject findSubject(String code) {
        for (int i = 0; i < subjectList.size(); i++) {
            Subject s = (Subject) subjectList.get(i);
            if (s.getSubjectCode().equals(code)) return s;
        }
        return null;
    }

    private Student findOrCreateStudent(String code) {
        for (int i = 0; i < studentList.size(); i++) {
            Student s = (Student) studentList.get(i);
            if (s.getStudentCode().equals(code)) return s;
        }
        Student s = new Student(code);
        studentList.add(studentList.size(), s);
        return s;
    }

    // --- Үндсэн үйлдлүүд ---
    public void showAllSubjects() {
        System.out.println("--- Subjects ---");
        for (int i = 0; i < subjectList.size(); i++) {
            System.out.println(subjectList.get(i));
        }
    }

    public void showAllMajors() {
        System.out.println("--- Majors ---");
        for (int i = 0; i < majorList.size(); i++) {
            System.out.println(majorList.get(i));
        }
    }

    public void showOverallAverageGPA() {
        double sum = 0; int cnt = 0;
        for (int i = 0; i < studentList.size(); i++) {
            Student s = (Student) studentList.get(i);
            sum += s.getGPA();
            cnt++;
        }
        System.out.println("Overall average GPA = " + (sum / cnt));
    }

    public void listStudentsWithMoreThan3F() {
        System.out.println("--- Students with >3 F ---");
        for (int i = 0; i < studentList.size(); i++) {
            Student s = (Student) studentList.get(i);
            if (s.countF() > 3) System.out.println(s);
        }
    }

    public void showGradesBySubject() {
        System.out.println("--- Grades by Subject ---");
        for (int i = 0; i < subjectList.size(); i++) {
            Subject subj = (Subject) subjectList.get(i);
            System.out.println(subj);
            for (int j = 0; j < studentList.size(); j++) {
                Student st = (Student) studentList.get(j);
                for (int k = 0; k < st.lessons.size(); k++) {
                    Lessons l = (Lessons) st.lessons.get(k);
                    if (l.getSubject().getSubjectCode().equals(subj.getSubjectCode())) {
                        System.out.println("   " + st.getStudentCode() + " - " + l);
                    }
                }
            }
        }
    }
}
