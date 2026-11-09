package uims;

/** Administrative staff or exam board member who publishes results and timetables. */
public class Administrator extends User {
    private String accessLevel;

    /**
     * Creates an administrator with the ADMINISTRATOR role.
     *
     * @param accessLevel for example "FULL"
     */
    public Administrator(String userId, String fullName, String email, String password,
                         String phoneNumber, String accessLevel) {
        super(userId, fullName, email, password, phoneNumber, Role.ADMINISTRATOR);
        this.accessLevel = accessLevel;
    }

    /**
     * Use case 2, part 2: the administrator approves marks.
     * Every pending result in the enrollment is published, then the student is notified.
     *
     * @param enrollment the enrollment whose results are approved
     * @param notifier   how the student is told
     * @return how many results were published
     * @throws IllegalStateException if the administrator is not logged in
     */
    public int approveResults(Enrollment enrollment, Notification notifier) {
        if (!isLoggedIn())
            throw new IllegalStateException("Administrator must log in first");
        int count = 0;
        for (StudentResult result : enrollment.getResults()) {
            if (!result.isPublished()) {
                result.publish();
                count++;
            }
        }
        if (count > 0) {
            notifier.send(enrollment.getStudent(), "Results published",
                    count + " result(s) for " + enrollment.getCourse().getCourseCode() + " are now available");
        }
        return count;
    }

    /**
     * Makes a timetable slot visible to students.
     *
     * @throws IllegalStateException if the administrator is not logged in
     */
    public void publishTimetable(Timetable slot) {
        slot.publish(this);
    }

    @Override
    public String getDashboard() {
        return "Admin Console: " + getFullName() + " | Access: " + accessLevel;
    }

    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
}
