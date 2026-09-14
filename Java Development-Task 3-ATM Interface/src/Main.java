import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.addAccount(new Account("AC1001", "Ravi Kumar", "1234", 5000.0));
        bank.addAccount(new Account("AC1002", "Priya Singh", "5678", 12000.0));
        bank.addAccount(new Account("AC1003", "Subham Behera", "0000", 25000.0));

        Scanner sc = new Scanner(System.in);
        ATM atm = new ATM(bank, sc);

        System.out.println("===== Welcome to the Java ATM =====");

        // Logout returns to login; Quit terminates the application.
        while (true) {
            Account account = atm.loginFlow();

            if (account == null) {
                System.out.println("Access Denied. Too many incorrect attempts.");
                break;
            }

            boolean continueApplication = atm.runSession(account);

            if (!continueApplication) {
                break;
            }

            System.out.println("\nReturning to login screen...");
        }

        sc.close();
    }
}
