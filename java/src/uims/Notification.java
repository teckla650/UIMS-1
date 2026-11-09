package uims;

/**
 * Contract for sending a message to a user.
 * Callers depend on this interface only, so the channel (email, SMS)
 * can change without touching the code that sends the message.
 */
public interface Notification {

    /**
     * Sends a message to a user.
     *
     * @param recipient the user who receives the message
     * @param title     a short subject line
     * @param message   the message body
     */
    void send(User recipient, String title, String message);

    /**
     * Names the channel used by this implementation.
     *
     * @return the channel name, for example "EMAIL"
     */
    String getChannel();
}
