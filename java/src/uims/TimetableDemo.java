package uims;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Demo of Department and Timetable: a slot stays hidden from the student until published.
 * Run with: java -cp out uims.TimetableDemo
 */
public class TimetableDemo {
    public static void main(String[] args) {
        Notification notifier = new EmailNotification();

        Department dept = new Department("D01", "Information Systems");
        Lecturer dr = new Lecturer("U002", "Dr. Omol", "omol@kcau.ac.ke", "lec456",
                "0700333444", "EMP-17", "Software Engineering");
        Course oot = new Course("C01", "CPP6101", "Object Oriented Technologies", 3, 30);
        oot.assignLecturer(dr);
        dept.addCourse(oot);
        dept.addLecturer(dr);
        System.out.println("Department: " + dept.getName() + " | courses: " + dept.getCourses().size()
                + " | lecturers: " + dept.getLecturers().size());

        Student student = new Student("U001", "Teckla Ndanu", "2205701@students.kcau.ac.ke", "pass123",
                "0700111222", "22/05701", new FeeStatement("F001", 50000, LocalDate.of(2026, 11, 1)));
        student.getFeeStatement().recordPayment(50000);
        student.login("pass123");
        student.registerForCourse(oot, "Sem 1", notifier);

        Administrator admin = new Administrator("U003", "Exam Board", "board@kcau.ac.ke", "adm789",
                "0700555666", "FULL");
        admin.login("adm789");

        Timetable slot = oot.scheduleSession("T01", DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(11, 0), "Lab 2");
        System.out.println("Slot created: " + slot.generate());
        System.out.println("Student sees " + student.viewTimetable().size() + " slot(s) before publishing");
        admin.publishTimetable(slot);
        System.out.println("Student sees " + student.viewTimetable().size() + " slot(s) after publishing");
        for (Timetable t : student.viewTimetable()) System.out.println("  " + t.generate());

        try {
            oot.scheduleSession("T02", DayOfWeek.TUESDAY, LocalTime.of(11, 0), LocalTime.of(9, 0), "Lab 2");
        } catch (IllegalArgumentException ex) {
            System.out.println("Rejected: " + ex.getMessage());
        }
    }
}
