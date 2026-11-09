package uims;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An academic department. It groups courses and lecturers (aggregation):
 * both can exist without the department object.
 */
public class Department {
    private final String departmentId;
    private String name;
    private final List<Course> courses = new ArrayList<>();
    private final List<Lecturer> lecturers = new ArrayList<>();

    public Department(String departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }

    /** Adds a course to this department and sets the course's department. */
    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
            course.setDepartment(this);
        }
    }

    /** Adds a lecturer to this department and sets the lecturer's department. */
    public void addLecturer(Lecturer lecturer) {
        if (!lecturers.contains(lecturer)) {
            lecturers.add(lecturer);
            lecturer.setDepartment(this);
        }
    }

    public String getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Course> getCourses() { return Collections.unmodifiableList(courses); }
    public List<Lecturer> getLecturers() { return Collections.unmodifiableList(lecturers); }
}
