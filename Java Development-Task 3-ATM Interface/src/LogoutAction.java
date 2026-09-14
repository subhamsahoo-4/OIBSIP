import java.util.Scanner;

/**
 * Logs out the currently authenticated user.
 *
 * This action only handles the user-facing logout behavior. The ATM session
 * controls the session lifecycle, keeping responsibilities separated.
 */
public class LogoutAction implements MenuAction {

    @Override
    public String label() {
        return "Logout";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        System.out.println(
                "You have been logged out, " + account.getHolderName() + "."
        );
    }

    @Override
    public boolean isLogout() {
        return true;
    }
}
