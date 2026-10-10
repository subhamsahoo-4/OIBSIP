import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable record of a single account operation.
 * Adding a new kind of transaction later means adding one enum constant here
 * — nothing else in the codebase needs to change (Open/Closed).
 */
public class Transaction {

    public enum Type { WITHDRAW, DEPOSIT, TRANSFER_OUT, TRANSFER_IN }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final Type type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(Type type, double amount, double balanceAfter, String description) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public Type getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("[%s] %-13s Rs.%-10.2f Balance: Rs.%-10.2f %s",
                timestamp.format(FMT), type, amount, balanceAfter, description);
    }
}
