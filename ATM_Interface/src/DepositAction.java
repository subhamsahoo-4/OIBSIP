import java.util.Scanner;

public class DepositAction implements MenuAction {

    @Override
    public String label() {
        return "Deposit";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        double amount = ConsoleInput.readAmount(sc, "Enter amount to deposit: ");
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        account.deposit(amount);
        System.out.printf("Deposit successful. New balance: Rs.%.2f%n", account.getBalance());
    }
}
