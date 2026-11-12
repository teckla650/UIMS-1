package uims;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**One scheduled class slot for a course. Students only see it after an administrator publishes it.*/
public class Timetable {
    private final String timetableId;
    private final Course course;
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String room;
    private Administrator publishedBy;
    private boolean published;

    /**Creates an unpublished slot */
    public Timetable(String timetableId, Course course, DayOfWeek dayOfWeek,
                     LocalTime startTime, LocalTime endTime, String room) {
        if (!endTime.isAfter(startTime))
            throw new IllegalArgumentException("End time must be after start time.");
        this.timetableId = timetableId;
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.published = false;
    }

    /** Returns the slot as one line of text. */
    public String generate() {
        return course.getCourseCode() + " | " + dayOfWeek + " "
                + startTime + "-" + endTime + " | Room " + room;
    }

    /**
     * Makes the slot visible and records who published it.
     *
     * @param admin the administrator publishing the slot, who must be logged in
     * @throws IllegalStateException if the administrator is not logged in
     */
    public void publish(Administrator admin) {
        if (!admin.isLoggedIn())
            throw new IllegalStateException("Administrator must log in first");
        this.publishedBy = admin;
        this.published = true;
    }

    public String getTimetableId() { return timetableId; }
    public Course getCourse() { return course; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getRoom() { return room; }
    public Administrator getPublishedBy() { return publishedBy; }
    public boolean isPublished() { return published; }
}
