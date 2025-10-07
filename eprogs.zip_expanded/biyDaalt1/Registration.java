package biyDaalt1;
import java.io.*;
import java.util.Scanner;
import dataStructures.ArrayLinearList;


public class Registration {
    public ArrayLinearList studentList;
    public ArrayLinearList subjectList;
    public ArrayLinearList majorList;

    public Registration() {
        studentList = new ArrayLinearList();
        subjectList = new ArrayLinearList();
        majorList = new ArrayLinearList();
    }

    public void loadSubjects(String fileName) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = input.readLine()) != null) {
                String[] values = line.split("/");
                if (values.length == 3) {
                    String code = values[0].trim();
                    String name = values[1].trim();
                    float credit = Float.parseFloat(values[2].trim());
                    subjectList.add(subjectList.size(), new Subject(code, name, credit));
                }
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Error loading subjects: " + e.getMessage());
        }
    }

    public void loadMajors(String fileName) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = input.readLine()) != null) {
                String[] values = line.split("/");
                if (values.length == 2) {
                    String code = values[0].trim();
                    String name = values[1].trim();
                    majorList.add(majorList.size(), new Major(code, name));
                }
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Error loading majors: " + e.getMessage());
        }
    }

    public void loadExams(String fileName) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = input.readLine()) != null) {
                String[] values = line.split("/");
                if (values.length == 3) {
                    String studentCode = values[0].trim();
                    String subjectCode = values[1].trim();
                    int score = Integer.parseInt(values[2].trim());

                    Student student = findStudent(studentCode);
                    if (student == null) {
                        student = new Student(studentCode);
                        studentList.add(studentList.size(), student);
                    }

                    Subject subject = findSubject(subjectCode);
                    if (subject != null) {
                        Lessons lesson = new Lessons(subject, score);
                        student.addLesson(lesson);
                    } else {
                        System.out.println("Subject not found: " + subjectCode);
                    }
                }
            }
            input.close();
        } catch (IOException e) {
            System.out.println("Error loading exams: " + e.getMessage());
        }
    }

    private Student findStudent(String code) {
        for (int i = 0; i < studentList.size(); i++) {
            Student s = (Student) studentList.get(i);
            if (s.getStudentCode().equals(code)) {
                return s;
            }
        }
        return null;
    }

    private Subject findSubject(String code) {
        for (int i = 0; i < subjectList.size(); i++) {
            Subject sub = (Subject) subjectList.get(i);
            if (sub.getSubjectCode().equals(code)) {
                return sub;
            }
        }
        return null;
    }

    private Major findMajor(String code) {
        for (int i = 0; i < majorList.size(); i++) {
            Major m = (Major) majorList.get(i);
            if (m.getMajorCode().equals(code)) {
                return m;
            }
        }
        return null;
    }

    public void saveExams(String fileName) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < studentList.size(); i++) {
                Student s = (Student) studentList.get(i);
                for (int j = 0; j < s.getLessons().size(); j++) {
                    Lessons l = (Lessons) s.getLessons().get(j);
                    output.write(s.getStudentCode() + "/" + l.getLearned().getSubjectCode() + "/" + l.getScore());
                    output.newLine();
                }
            }
            output.close();
        } catch (IOException e) {
            System.out.println("Error saving exams: " + e.getMessage());
        }
    }

    public void showAllSubjects() {
        System.out.println("Niit hicheel:");
        for (int i = 0; i < subjectList.size(); i++) {
            System.out.println(subjectList.get(i));
        }
    }

    public void showAllMajors() {
        System.out.println("Niit mergejil:");
        for (int i = 0; i < majorList.size(); i++) {
            System.out.println(majorList.get(i));
        }
    }

    public float calculateAverageGPA() {
        if (studentList.size() == 0) return 0.0f;
        float totalGPA = 0.0f;
        for (int i = 0; i < studentList.size(); i++) {
            Student s = (Student) studentList.get(i);
            totalGPA += s.getGPA();
        }
        return totalGPA / studentList.size();
    }

    public void showFailingStudents() {
        System.out.println("Гурваас дээш хичээлд “F” үнэлгээ авсан хасагдах оюутан: ");
        for (int i = 0; i < studentList.size(); i++) {
            Student s = (Student) studentList.get(i);
            if (s.countFGrades() >= 3) {
                System.out.println(s);
            }
        }
    }

    public void showGradesBySubject() {
        for (int i = 0; i < subjectList.size(); i++) {
            Subject sub = (Subject) subjectList.get(i);
            System.out.println(sub.getSubjectName() + " hicheeliin onoo");
            for (int j = 0; j < studentList.size(); j++) {
                Student s = (Student) studentList.get(j);
                for (int k = 0; k < s.getLessons().size(); k++) {
                    Lessons l = (Lessons) s.getLessons().get(k);
                    if (l.getLearned().getSubjectCode().equals(sub.getSubjectCode())) {
                        System.out.println(s.getStudentCode() + ": " + l.getScore() + " (" + l.getGrade() + ")");
                    }
                }
            }
            System.out.println();
        }
    }

    public void showGradesByMajor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Mergejliin code: ,(blank for all)");
        String majorCode = scanner.nextLine().trim();
        
        if (majorCode.isEmpty()) {
            for (int i = 0; i < majorList.size(); i++) {
                Major m = (Major) majorList.get(i);
                showGradesForMajor(m.getMajorCode());
            }
        } else {
            showGradesForMajor(majorCode);
        }
    }

    private void showGradesForMajor(String majorCode) {
        Major major = findMajor(majorCode);
        if (major == null) {
            System.out.println("Major not found: " + majorCode);
            return;
        }
        String numericCode = "";
        if (majorCode.equals("IT")) {
            numericCode = "17";
        } else if (majorCode.equals("KAB")) {
            numericCode = "18";
        } else if (majorCode.equals("IoT")) {
            numericCode = "19";
        } else {
            System.out.println("No numeric mapping for major: " + majorCode);
            return;
        }
        System.out.println("Grades for Major " + major.getMajorName() + ":");
        for (int j = 0; j < studentList.size(); j++) {
            Student s = (Student) studentList.get(j);
            if (s.getStudentCode().length() >= 5 && s.getStudentCode().substring(3, 5).equals(numericCode)) {
                System.out.println(s + " Lessons:");
                for (int k = 0; k < s.getLessons().size(); k++) {
                    Lessons l = (Lessons) s.getLessons().get(k);
                    System.out.println("  " + l);
                }
            }
        }
        System.out.println();
    }
    public static void printMenu() {
    	System.out.println("\n----------^Menu^----------");
        System.out.println("1. Нийт хичээлүүдийн жагсаалтыг харуулах");
        System.out.println("2. Нийт мэргэжлүүдийн жагсаалтыг харуулах");
        System.out.println("3. Нийт оюутны дундаж голч дүнг харуулах");
        System.out.println("4. Гурваас дээш хичээлд “F” үнэлгээ авсан хасагдах оюутан");
        System.out.println("5. Хичээл бүрээр оюутнуудын дүнгийн жагсаалтыг харуулах");
        System.out.println("6. Мэргэжил бүрээр оюутнуудын дүнгийн жагсаалтыг харуулах");
        System.out.println("7. Save and Exit");
        System.out.print("----------Choose an option----------\n");
    }

    public static void main(String[] args) {
        Registration reg = new Registration();
        reg.loadSubjects("C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\eprogs.zip_expanded\\biyDaalt1\\Subjects.txt");
        reg.loadMajors("C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\eprogs.zip_expanded\\biyDaalt1\\Professions.txt");
        reg.loadExams("C:\\Users\\orgil\\OneDrive\\Documents\\GitHub\\Classes-repo\\eprogs.zip_expanded\\biyDaalt1\\Exams.txt");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    reg.showAllSubjects();
                    break;
                case 2:
                    reg.showAllMajors();
                    break;
                case 3:
                    System.out.println("Dundaj GPA: " + reg.calculateAverageGPA());
                    break;
                case 4:
                    reg.showFailingStudents();
                    break;
                case 5:
                    reg.showGradesBySubject();
                    break;
                case 6:
                    reg.showGradesByMajor();
                    break;
                case 7:
                    reg.saveExams("Exams.txt");
                    System.out.println("Data saved. Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}