package uims;

import java.time.LocalDate;
import java.util.function.Supplier;

/**Runs the 14 manual test cases for the two use cases and prints a results table. No test library is needed. Run with: java -cp out uims.TestRunner*/
public class TestRunner {
    static int passed = 0, total = 0;

    /**
     * Runs one test and prints a table row.
     * An exception thrown by the action is reported by its class name, so a test
     * can expect an exception by naming it as the expected value.
     */
    static void check(String id, String input, String expected, Supplier<String> action) {
        String actual;
        try { actual = action.get(); }
        catch (Exception e) { actual = e.getClass().getSimpleName(); }
        boolean ok = expected.equals(actual);
        total++; if (ok) passed++;
        System.out.printf("%-5s | %-48s | %-24s | %-24s | %s%n", id, input, expected, actual, ok ? "PASS" : "FAIL");
    }

    /** Creates a student whose fees have the given amount paid. Each test gets fresh objects. */
    static Student newStudent(double paid) {
        FeeStatement f = new FeeStatement("F", 50000, LocalDate.of(2026, 12, 1));
        if (paid > 0) f.recordPayment(paid);
        return new Student("U1", "Ann", "ann@x.ke", "pass123", "0700", "26/001", f);
    }
    static Lecturer newLecturer() { return new Lecturer("U2", "Dr O", "o@x.ke", "lec456", "0711", "E1", "SE"); }
    static Course newCourse(int cap) { return new Course("C1", "CPP6101", "OOT", 3, cap); }
    /** A notifier that prints nothing, so test output stays readable. */
    static Notification quiet = new Notification() {
        public void send(User to, String title, String msg) { }
        public String getChannel() { return "NONE"; }
    };

    public static void main(String[] args) {
        System.out.printf("%-5s | %-48s | %-24s | %-24s | %s%n", "ID", "Input", "Expected", "Actual", "Result");
        System.out.println("-".repeat(125));

        // Use case 1: Student registers for a unit
        check("T1", "Login with wrong password", "false", () -> String.valueOf(newStudent(0).login("bad")));
        check("T2", "Register without logging in", "IllegalStateException", () -> {
            return String.valueOf(newStudent(50000).registerForCourse(newCourse(5), "S1", quiet)); });
        check("T3", "Login OK, fees cleared, course open", "ACTIVE", () -> {
            Student s = newStudent(50000); s.login("pass123");
            return s.registerForCourse(newCourse(5), "S1", quiet).getStatus(); });
        check("T4", "Login OK, fee balance 30000 outstanding", "IllegalStateException", () -> {
            Student s = newStudent(20000); s.login("pass123");
            return String.valueOf(s.registerForCourse(newCourse(5), "S1", quiet)); });
        check("T5", "Register for same course twice", "IllegalStateException", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            s.registerForCourse(c, "S1", quiet);
            return String.valueOf(s.registerForCourse(c, "S1", quiet)); });
        check("T6", "Register when course capacity = 0", "IllegalStateException", () -> {
            Student s = newStudent(50000); s.login("pass123");
            return String.valueOf(s.registerForCourse(newCourse(0), "S1", quiet)); });
        check("T7", "After T3, course enrolled-student count", "1", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            s.registerForCourse(c, "S1", quiet);
            return String.valueOf(c.getEnrolledStudents().size()); });

        // Use case 2: Lecturer enters marks
        check("T8", "Assigned lecturer enters 78", "A", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); c.assignLecturer(l); l.login("lec456");
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            return l.enterMarks(e, "CAT", 78).getGrade(); });
        check("T9", "Lecturer enters score 105 (invalid)", "IllegalArgumentException", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); c.assignLecturer(l); l.login("lec456");
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            return String.valueOf(l.enterMarks(e, "CAT", 105)); });
        check("T10", "Lecturer NOT assigned to course enters marks", "SecurityException", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); l.login("lec456");   // never assigned
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            return String.valueOf(l.enterMarks(e, "CAT", 60)); });
        check("T11", "Score 40 -> grade (boundary)", "D", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); c.assignLecturer(l); l.login("lec456");
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            return l.enterMarks(e, "CAT", 40).getGrade(); });
        check("T12", "Score 39.9 -> grade (boundary)", "E", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); c.assignLecturer(l); l.login("lec456");
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            return l.enterMarks(e, "CAT", 39.9).getGrade(); });
        check("T13", "Student views results before board approval", "0", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); c.assignLecturer(l); l.login("lec456");
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            l.enterMarks(e, "CAT", 78);
            return String.valueOf(s.viewResults().size()); });
        check("T14", "Board approves, student views results", "1", () -> {
            Student s = newStudent(50000); s.login("pass123"); Course c = newCourse(5);
            Lecturer l = newLecturer(); c.assignLecturer(l); l.login("lec456");
            Administrator a = new Administrator("U3", "Board", "b@x.ke", "adm789", "0722", "FULL"); a.login("adm789");
            Enrollment e = s.registerForCourse(c, "S1", quiet);
            l.enterMarks(e, "CAT", 78); a.approveResults(e, quiet);
            return String.valueOf(s.viewResults().size()); });

        System.out.println("-".repeat(125));
        System.out.println("Passed " + passed + " of " + total);
    }
}
