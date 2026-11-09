package uims;

/** Sends notifications by email. The email is simulated by printing to the console. */
public class EmailNotification implements Notification {

    @Override
    public void send(User recipient, String title, String message) {
        System.out.println("  [EMAIL to " + recipient.getEmail() + "] " + title + ": " + message);
    }

    @Override
    public String getChannel() {
        return "EMAIL";
    }
}
