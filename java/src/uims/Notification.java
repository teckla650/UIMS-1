package uims;

/**Contract for sending a message to a user. Callers depend on this interface only, so the channel (email, SMS) can change without touching the code that sends the message */
public interface Notification {

    /**Sends a message to a user.*/
    void send(User recipient, String title, String message);

    /**Names the channel used by this implementation.*/
    String getChannel();
}
