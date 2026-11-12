package uims;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A learner who registers for courses and views fees, results and timetable. */
public class Student extends User {
    private final String admissionNumber;
    private final FeeStatement feeStatement;
    private final List<Enrollment> enrollments = new ArrayList<>();

    // Creates a student with the STUDENT role. @param feeStatement this student's fee record//
    public Student(String userId, String fullName, String email, String password,
                   String phoneNumber, String admissionNumber, FeeStatement feeStatement) {
        super(userId, fullName, email, password, phoneNumber, Role.STUDENT);
        this.admissionNumber = admissionNumber;
        this.feeStatement = feeStatement;
    }

    // Use case 1: the student registers for a unit. The checks run in this order: login, fees, duplicate registration, capacity. If all pass, an Enrollment is created and the student is notified.
    //
    public Enrollment registerForCourse(Course course, String semester, Notification notifier) {
        if (!isLoggedIn())
            throw new IllegalStateException("Student must log in first");
        if (!feeStatement.isCleared())
            throw new IllegalStateException("Fee balance not cleared: " + feeStatement.calculateBalance());
        if (course.hasStudent(this))
            throw new IllegalStateException("Already registered for " + course.getCourseCode());
        if (course.isFull())
            throw new IllegalStateException(course.getCourseCode() + " is full");

        Enrollment enrollment = new Enrollment(
                "ENR-" + admissionNumber + "-" + course.getCourseCode(), this, course, semester);
        enrollments.add(enrollment);
        course.addEnrollment(enrollment);
        notifier.send(this, "Registration confirmed",
                "You are registered for " + course.getCourseCode() + " - " + course.getCourseName());
        return enrollment;
    }

    /** Returns only the results an administrator has published. */
    public List<StudentResult> viewResults() {
        List<StudentResult> published = new ArrayList<>();
        for (Enrollment e : enrollments) {
            for (StudentResult r : e.getResults()) {
                if (r.isPublished()) published.add(r);
            }
        }
        return published;
    }

    /** Returns the fee statement as one line of text. */
    public String viewFeeStatement() {
        return feeStatement.generateStatement();
    }

    /** Returns the published timetable slots for the courses this student is taking. */
    public List<Timetable> viewTimetable() {
        List<Timetable> slots = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (!e.isActive()) continue;
            for (Timetable t : e.getCourse().getTimetable()) {
                if (t.isPublished()) slots.add(t);
            }
        }
        return slots;
    }

    @Override
    public String getDashboard() {
        return "Student Portal: " + getFullName() + " | Units: " + enrollments.size();
    }

    public String getAdmissionNumber() { return admissionNumber; }
    public FeeStatement getFeeStatement() { return feeStatement; }
    public List<Enrollment> getEnrollments() { return Collections.unmodifiableList(enrollments); }
}
