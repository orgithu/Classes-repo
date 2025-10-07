package biyDaalt1;
import dataStructures.Chain;

//Student.java
public class Student {
	private String studentCode;
	private float GPA;
	private Chain lessons;

	public Student(String studentCode) {
		this.studentCode = studentCode;
		this.lessons = new Chain();
		this.GPA = 0.0f;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public float getGPA() {
		return GPA;
	}

	public Chain getLessons() {
		return lessons;
	}

	public void addLesson(Lessons lesson) {
		lessons.add(lessons.size(),lesson);
		calculateGPA();
	}

	private void calculateGPA() {
		if (lessons.size() == 0) {
			GPA = 0.0f;
			return;
		}
		float totalPoints = 0.0f;
		float totalCredits = 0.0f;
		for (int i = 0; i < lessons.size(); i++) {
			Lessons l = (Lessons) lessons.get(i);
			totalPoints += l.getGPAValue() * l.getLearned().getCredit();
			totalCredits += l.getLearned().getCredit();
		}
		GPA = totalPoints / totalCredits;
	}

	public int countFGrades() {
		int count = 0;
		for (int i = 0; i < lessons.size(); i++) {
			Lessons l = (Lessons) lessons.get(i);
			if (l.getGrade().equals("F")) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String toString() {
		return studentCode + " (GPA: " + GPA + ")";
	}
}