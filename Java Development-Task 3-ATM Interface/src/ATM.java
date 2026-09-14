import java.util.List;
import java.util.Scanner;

public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int PIN_LENGTH = 4;

    private final BankOperations bank;
    private final Scanner sc;
    private final List<MenuAction> actions;

    public ATM(BankOperations bank, Scanner sc) {
        this.bank = bank;
        this.sc = sc;
        this.actions = List.of(
                new HistoryAction(),
                new BalanceAction(),
                new WithdrawAction(),
                new DepositAction(),
                new TransferAction(),
                new LogoutAction(),
                new QuitAction()
        );
    }

    public Account loginFlow() {
        int failedAttempts = 0;

        while (failedAttempts < MAX_LOGIN_ATTEMPTS) {
            System.out.print("Enter User ID: ");
            String id = sc.nextLine().trim();

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine().trim();

            // PIN must contain exactly 4 digits.
            // Incorrect PIN length is an input-format error and does not
            // consume one of the three authentication attempts.
            if (!pin.matches("\\d{" + PIN_LENGTH + "}")) {
                System.out.println(
                        "Invalid PIN. Please enter a PIN of exactly "
                                + PIN_LENGTH + " digits."
                );
                continue;
            }

            Account account = bank.authenticate(id, pin);

            if (account != null) {
                System.out.println(
                        "Login successful. Welcome, "
                                + account.getHolderName() + "!"
                );
                return account;
            }

            failedAttempts++;

            System.out.println(
                    "Invalid credentials. Attempts remaining: "
                            + (MAX_LOGIN_ATTEMPTS - failedAttempts)
            );
        }

        return null;
    }

    public boolean runSession(Account account) {
        while (true) {
            System.out.println("\n===== Main Menu (" + account.getHolderName() + ") =====");
            for (int i = 0; i < actions.size(); i++) {
                System.out.println((i + 1) + ". " + actions.get(i).label());
            }
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

            if (choice < 1 || choice > actions.size()) {
                System.out.println("Invalid option.");
                continue;
            }

            MenuAction chosen = actions.get(choice - 1);
            chosen.execute(account, bank, sc);

            if (chosen.isExit()) {
                return false; // Quit: terminate the ATM application
            }

            if (chosen.isLogout()) {
                return true;  // Logout: return to the login screen
            }
        }
    }
}