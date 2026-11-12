package uims;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Base class for everyone who logs in to the system. Student, Lecturer and Administrator extend it, so identity and session behaviour is written once.//
public abstract class User {
    private final String userId;
    private String fullName;
    private String email;
    private final String passwordHash;
    private String phoneNumber;
    private final Role role;
    private boolean loggedIn;

    // Creates a user who is not yet logged in. The password is hashed here and the plain text is never kept. @param role the role fixed by the subclass (STUDENT, LECTURER or ADMINISTRATOR)//
    protected User(String userId, String fullName, String email,
                   String password, String phoneNumber, Role role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = hash(password);
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.loggedIn = false;
    }

    /**
     * Checks the password and opens a session. A failed attempt leaves the
     * user logged out.
     *
     * @param password the plain-text password typed by the user
     * @return true if the password is correct
     */
    public boolean login(String password) {
        loggedIn = passwordHash.equals(hash(password));
        return loggedIn;
    }

    /** Ends the current session. */
    public void logout() {
        loggedIn = false;
    }

    /**
     * Updates the contact details. A null or blank value keeps the old one.
     */
    public void updateProfile(String newName, String newEmail, String newPhone) {
        if (newName != null && !newName.isBlank()) this.fullName = newName;
        if (newEmail != null && !newEmail.isBlank()) this.email = newEmail;
        if (newPhone != null && !newPhone.isBlank()) this.phoneNumber = newPhone;
    }

    /**
     * Describes this user's home screen. Each role overrides it with its own text.
     *
     * @return the dashboard text
     */
    public abstract String getDashboard();

    /** Returns the SHA-256 hash of the text as lowercase hex. */
    private static String hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is required on every Java platform, so this should never happen.
            throw new IllegalStateException(e);
        }
    }

    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Role getRole() { return role; }
    public boolean isLoggedIn() { return loggedIn; }
}
