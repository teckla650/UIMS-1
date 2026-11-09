package uims;

import java.time.LocalDate;

/**
 * Console demo of the two use cases, with sample data.
 * Run with: java -cp out uims.Main
 */
public class Main {
    public static void main(String[] args) {
        Notification email = new EmailNotification();

        // Sample users and course
        Student teckla = new Student("U001", "Teckla Ndanu", "2205701@students.kcau.ac.ke", "pass123",
                "0700111222", "22/05701", new FeeStatement("F001", 50000, LocalDate.of(2026, 11, 1)));
        Lecturer dr = new Lecturer("U002", "Dr. Omol", "omol@kcau.ac.ke", "lec456",
                "0700333444", "EMP-17", "Software Engineering");
        Administrator board = new Administrator("U003", "Exam Board", "board@kcau.ac.ke", "adm789",
                "0700555666", "FULL");
        Course oot = new Course("C01", "CPP6101", "Object Oriented Technologies", 3, 30);
        oot.assignLecturer(dr);

        // Polymorphism: one getDashboard() call, a different result for each role
        System.out.println("Polymorphism: getDashboard() on User references");
        User[] users = { teckla, dr, board };
        for (User u : users) System.out.println("  " + u.getDashboard());

        // Use case 1: Student registers for a unit
        System.out.println("\nUSE CASE 1: Student registers for a unit");
        teckla.login("pass123");
        try {
            teckla.registerForCourse(oot, "Sem 1", email);          // expected to be blocked: fees not paid yet
        } catch (IllegalStateException ex) {
            System.out.println("  Blocked: " + ex.getMessage());
        }
        teckla.getFeeStatement().recordPayment(50000);
        System.out.println("  " + teckla.viewFeeStatement());
        Enrollment enr = teckla.registerForCourse(oot, "Sem 1", new SmsNotification());
        System.out.println("  Enrollment " + enr.getEnrollmentId() + " status: " + enr.getStatus());
        System.out.println("  Students in " + oot.getCourseCode() + ": " + oot.getEnrolledStudents().size());

        // Use case 2: Enter and approve student marks
        System.out.println("\nUSE CASE 2: Enter and approve student marks");
        dr.login("lec456");
        board.login("adm789");
        StudentResult r = dr.enterMarks(enr, "CAT 1", 78);
        System.out.println("  Entered: " + r);
        System.out.println("  Student sees " + teckla.viewResults().size() + " result(s) before approval");
        int n = board.approveResults(enr, email);
        System.out.println("  Board published " + n + " result(s)");
        System.out.println("  Student now sees: " + teckla.viewResults());
    }
}
