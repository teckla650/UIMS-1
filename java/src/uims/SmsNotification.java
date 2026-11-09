package uims;

/** Sends notifications by SMS. The message is simulated by printing to the console. */
public class SmsNotification implements Notification {

    @Override
    public void send(User recipient, String title, String message) {
        System.out.println("  [SMS to " + recipient.getPhoneNumber() + "] " + title + ": " + message);
    }

    @Override
    public String getChannel() {
        return "SMS";
    }
}
