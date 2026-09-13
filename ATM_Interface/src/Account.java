import java.util.ArrayList;
import java.util.List;

/**
 * Owns its own state (balance, history, PIN). Nothing outside this class
 * mutates balance directly — every change goes through a validated method
 * (Encapsulation / Single Responsibility).
 */
public class Account {

    private final String accountId;
    private final String holderName;
    private final String pin;
    private double balance;
    private final List<Transaction> history = new ArrayList<>();

    public Account(String accountId, String holderName, String pin, double balance) {
        this.accountId = accountId;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean validatePin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    /** Public withdrawal — validates funds and logs a WITHDRAW transaction. */
    public boolean withdraw(double amount) {
        if (!debit(amount)) {
            return false;
        }
        history.add(new Transaction(Transaction.Type.WITHDRAW, amount, balance, "Cash withdrawal"));
        return true;
    }

    /** Public deposit — logs a DEPOSIT transaction. */
    public void deposit(double amount) {
        if (amount <= 0) return;
        credit(amount);
        history.add(new Transaction(Transaction.Type.DEPOSIT, amount, balance, "Cash deposit"));
    }

    // ---- Package-private primitives, used only by Bank.transfer() so the
    // ---- balance-check logic (debit) lives in exactly one place, while the
    // ---- caller (Bank) decides which transaction type to log. ----

    boolean debit(double amount) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        return true;
    }

    void credit(double amount) {
        if (amount <= 0) return;
        balance += amount;
    }

    void logTransferOut(double amount, String toAccountId) {
        history.add(new Transaction(Transaction.Type.TRANSFER_OUT, amount, balance, "Transfer to " + toAccountId));
    }

    void logTransferIn(double amount, String fromAccountId) {
        history.add(new Transaction(Transaction.Type.TRANSFER_IN, amount, balance, "Transfer from " + fromAccountId));
    }

    public String getAccountId() { return accountId; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public List<Transaction> getHistory() { return history; }
}
