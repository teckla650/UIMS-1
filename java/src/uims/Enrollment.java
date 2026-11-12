package uims;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**Links one Student to one Course. It also owns the student's results for that course (composition): results are created here and cannot exist without it */
public class Enrollment {
    private final String enrollmentId;
    private final Student student;
    private final Course course;
    private final LocalDate enrollmentDate;
    private final String semester;
    private String status;
    private final List<StudentResult> results = new ArrayList<>();

    /** Creates an active enrollment dated today. */
    public Enrollment(String enrollmentId, Student student, Course course, String semester) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.semester = semester;
        this.enrollmentDate = LocalDate.now();
        this.status = "ACTIVE";
    }

    /**
     * Creates a pending result for this enrollment.
     *
     * @param assessmentType for example "CAT 1"
     * @param score          a score from 0 to 100
     * @return the new result
     * @throws IllegalArgumentException if the score is outside 0 to 100
     */
    public StudentResult addResult(String assessmentType, double score) {
        StudentResult result = new StudentResult(enrollmentId + "-R" + (results.size() + 1),
                assessmentType, score);
        results.add(result);
        return result;
    }

    /** Marks the enrollment as dropped. */
    public void dropCourse() {
        this.status = "DROPPED";
    }

    /** Returns true while the student is still taking the course. */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public String getEnrollmentId() { return enrollmentId; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public String getSemester() { return semester; }
    public String getStatus() { return status; }

    /** Returns a read-only view, so results can only be added through addResult(). */
    public List<StudentResult> getResults() {
        return Collections.unmodifiableList(results);
    }
}
