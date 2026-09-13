import java.util.Scanner;

public class TransferAction implements MenuAction {

    @Override
    public String label() {
        return "Transfer";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        System.out.print("Enter recipient account ID: ");
        String toId = sc.nextLine().trim();

        if (toId.equals(account.getAccountId())) {
            System.out.println("Cannot transfer to your own account.");
            return;
        }

        double amount = ConsoleInput.readAmount(sc, "Enter amount to transfer: ");
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }

        if (bank.transfer(account, toId, amount)) {
            System.out.printf("Transfer successful. New balance: Rs.%.2f%n", account.getBalance());
        } else {
            System.out.println("Transfer failed: insufficient funds or invalid recipient ID.");
        }
    }
}
