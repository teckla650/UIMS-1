package uims;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Teaching staff who enter marks for the courses they teach. */
public class Lecturer extends User {
    private final String employeeNumber;
    private String specialization;
    private final List<Course> courses = new ArrayList<>();
    private Department department;

    /** Creates a lecturer with the LECTURER role. */
    public Lecturer(String userId, String fullName, String email, String password,
                    String phoneNumber, String employeeNumber, String specialization) {
        super(userId, fullName, email, password, phoneNumber, Role.LECTURER);
        this.employeeNumber = employeeNumber;
        this.specialization = specialization;
    }

    /** Adds a course to the list taught. Package-private so only Course.assignLecturer() uses it. */
    void addCourse(Course course) {
        if (!courses.contains(course)) courses.add(course);
    }

    /** Sets the department. Package-private so only Department.addLecturer() uses it. */
    void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Use case 2, part 1: the lecturer enters marks.
     * The checks run in this order: login, assigned to the course, enrollment active.
     * The result is saved as pending until an Administrator approves it.
     *
     * @param enrollment     the student's enrollment in the course
     * @param assessmentType for example "CAT 1"
     * @param score          a score from 0 to 100
     * @return the new, unpublished result
     * @throws IllegalStateException    if the lecturer is not logged in or the enrollment is not active
     * @throws SecurityException        if the lecturer is not assigned to the course
     * @throws IllegalArgumentException if the score is outside 0 to 100
     */
    public StudentResult enterMarks(Enrollment enrollment, String assessmentType, double score) {
        if (!isLoggedIn())
            throw new IllegalStateException("Lecturer must log in first");
        if (enrollment.getCourse().getLecturer() != this)
            throw new SecurityException("You are not assigned to " + enrollment.getCourse().getCourseCode());
        if (!enrollment.isActive())
            throw new IllegalStateException("Enrollment is not active");
        return enrollment.addResult(assessmentType, score);
    }

    @Override
    public String getDashboard() {
        return "Lecturer Portal: " + getFullName() + " | Courses taught: " + courses.size();
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public Department getDepartment() { return department; }
    public List<Course> getCourses() { return Collections.unmodifiableList(courses); }
}
