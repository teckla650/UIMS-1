package uims;

/**
 * The outcome of one assessment. An Enrollment creates and owns its results
 * (composition), so the constructor is package-private.
 * A new result is pending until an administrator publishes it.
 */
public class StudentResult {
    private final String resultId;
    private final String assessmentType;
    private double score;
    private String grade;
    private boolean published;

    /**
     * Creates a pending result and works out its grade.
     *
     * @throws IllegalArgumentException if the score is outside 0 to 100
     */
    StudentResult(String resultId, String assessmentType, double score) {
        this.resultId = resultId;
        this.assessmentType = assessmentType;
        updateScore(score);
    }

    /**
     * Sets the score and recalculates the grade.
     *
     * @param newScore a score from 0 to 100
     * @throws IllegalArgumentException if the score is outside 0 to 100
     */
    public void updateScore(double newScore) {
        if (newScore < 0 || newScore > 100)
            throw new IllegalArgumentException("Score must be between 0 and 100");
        this.score = newScore;
        this.grade = calculateGrade();
    }

    /**
     * Converts the score to a grade: A from 70, B from 60, C from 50,
     * D from 40, otherwise E.
     */
    public String calculateGrade() {
        if (score >= 70) return "A";
        if (score >= 60) return "B";
        if (score >= 50) return "C";
        if (score >= 40) return "D";
        return "E";
    }

    /** Makes the result visible to the student. Only an Administrator triggers this. */
    void publish() {
        this.published = true;
    }

    public String getResultId() { return resultId; }
    public String getAssessmentType() { return assessmentType; }
    public double getScore() { return score; }
    public String getGrade() { return grade; }
    public boolean isPublished() { return published; }

    @Override
    public String toString() {
        return assessmentType + ": " + score + " (" + grade + ")"
                + (published ? "" : " [pending approval]");
    }
}
