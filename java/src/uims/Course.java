package uims;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A unit offered by a department. It has one lecturer and many enrollments. */
public class Course {
    private final String courseId;
    private final String courseCode;
    private String courseName;
    private final int creditUnits;
    private final int capacity;
    private Lecturer lecturer;
    private Department department;
    private final List<Enrollment> enrollments = new ArrayList<>();
    private final List<Timetable> timetable = new ArrayList<>();

    /**
     * Creates a course with no lecturer, department or students yet.
     *
     * @param capacity the most students that may enrol
     */
    public Course(String courseId, String courseCode, String courseName, int creditUnits, int capacity) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditUnits = creditUnits;
        this.capacity = capacity;
    }

    /** Sets the lecturer for this course and adds the course to the lecturer's list. */
    public void assignLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
        lecturer.addCourse(this);
    }

    /** Returns true when no more students can enrol. */
    public boolean isFull() {
        return enrollments.size() >= capacity;
    }

    /** Returns true if the student has an active enrollment in this course. */
    public boolean hasStudent(Student student) {
        return enrollments.stream().anyMatch(e -> e.isActive() && e.getStudent() == student);
    }

    /** Adds an enrollment. Package-private so only Student.registerForCourse() uses it. */
    void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    /** Returns the students whose enrollment is still active. */
    public List<Student> getEnrolledStudents() {
        List<Student> students = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.isActive()) students.add(e.getStudent());
        }
        return students;
    }

    /**
     * Creates an unpublished timetable slot for this course.
     *
     * @return the new slot, which an Administrator can then publish
     * @throws IllegalArgumentException if the end time is not after the start time
     */
    public Timetable scheduleSession(String id, DayOfWeek day, LocalTime start, LocalTime end, String room) {
        Timetable slot = new Timetable(id, this, day, start, end, room);
        timetable.add(slot);
        return slot;
    }

    /** Sets the department. Package-private so only Department.addCourse() uses it. */
    void setDepartment(Department department) {
        this.department = department;
    }

    public String getCourseId() { return courseId; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getCreditUnits() { return creditUnits; }
    public int getCapacity() { return capacity; }
    public Lecturer getLecturer() { return lecturer; }
    public Department getDepartment() { return department; }
    public List<Enrollment> getEnrollments() { return Collections.unmodifiableList(enrollments); }
    public List<Timetable> getTimetable() { return Collections.unmodifiableList(timetable); }
}
