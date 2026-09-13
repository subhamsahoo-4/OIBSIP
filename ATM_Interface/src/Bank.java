import java.util.HashMap;
import java.util.Map;

/**
 * Owns the account registry and is the only class that coordinates two
 * accounts at once (transfer). Account lookup stays internal — callers only
 * ever see the BankOperations abstraction, never this registry directly.
 */
public class Bank implements BankOperations {

    private final Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    @Override
    public Account authenticate(String accountId, String pin) {
        Account acc = accounts.get(accountId);
        if (acc != null && acc.validatePin(pin)) {
            return acc;
        }
        return null;
    }

    @Override
    public boolean transfer(Account from, String toAccountId, double amount) {
        Account to = accounts.get(toAccountId);
        if (from == null || to == null || from == to) {
            return false;
        }
        if (!from.debit(amount)) {
            return false;
        }
        to.credit(amount);
        from.logTransferOut(amount, to.getAccountId());
        to.logTransferIn(amount, from.getAccountId());
        return true;
    }
}
