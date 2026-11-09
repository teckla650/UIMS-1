package uims;

import java.time.LocalDate;

/**
 * A student's fee record. The balance is calculated from the total and the
 * amount paid, so no other class can set it directly.
 */
public class FeeStatement {
    private final String statementId;
    private final double totalAmount;
    private double amountPaid;
    private final LocalDate dueDate;

    /**
     * Creates a statement with nothing paid yet.
     *
     * @param totalAmount the total fees charged
     * @param dueDate     the date the fees must be cleared by
     */
    public FeeStatement(String statementId, double totalAmount, LocalDate dueDate) {
        this.statementId = statementId;
        this.totalAmount = totalAmount;
        this.amountPaid = 0;
        this.dueDate = dueDate;
    }

    /**
     * Records a payment.
     *
     * @param amount the amount paid, which must be greater than zero
     * @throws IllegalArgumentException if the amount is zero or negative
     */
    public void recordPayment(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Payment must be greater than zero");
        amountPaid += amount;
    }

    /** Returns the amount still owed. */
    public double calculateBalance() {
        return totalAmount - amountPaid;
    }

    /** Returns true when nothing is owed. */
    public boolean isCleared() {
        return calculateBalance() <= 0;
    }

    /** Returns the statement as one line of text. */
    public String generateStatement() {
        return String.format("Statement %s | Total: %.2f | Paid: %.2f | Balance: %.2f | Due: %s",
                statementId, totalAmount, amountPaid, calculateBalance(), dueDate);
    }

    public String getStatementId() { return statementId; }
    public double getTotalAmount() { return totalAmount; }
    public double getAmountPaid() { return amountPaid; }
    public LocalDate getDueDate() { return dueDate; }
}
