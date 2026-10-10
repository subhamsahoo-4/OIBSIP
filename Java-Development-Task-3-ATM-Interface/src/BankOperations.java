/**
 * Abstraction that ATM depends on. Bank implements this; ATM never refers to
 * the concrete Bank type, so a fake/test implementation could stand in
 * without changing ATM at all (Dependency Inversion, Interface Segregation —
 * this only exposes what a session actually needs, not the account registry).
 */
public interface BankOperations {
    Account authenticate(String accountId, String pin);
    boolean transfer(Account from, String toAccountId, double amount);
}
