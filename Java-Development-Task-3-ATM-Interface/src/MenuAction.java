import java.util.Scanner;

/**
 * One menu option = one implementation of this interface. ATM just holds a
 * list of these and loops — adding a new option later means writing a new
 * class, never touching ATM (Open/Closed). Narrow on purpose: implementers
 * only need execute() (Interface Segregation).
 */
public interface MenuAction {
    String label();
    void execute(Account account, BankOperations bank, Scanner sc);

    /** True only when the action should terminate the entire ATM application. */
    default boolean isExit() {
        return false;
    }

    /** True only when the action should log out the current user. */
    default boolean isLogout() {
        return false;
    }
}
