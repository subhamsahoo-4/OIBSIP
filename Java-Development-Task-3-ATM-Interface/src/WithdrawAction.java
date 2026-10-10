import java.util.Scanner;

public class WithdrawAction implements MenuAction {

    @Override
    public String label() {
        return "Withdraw";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        double amount = ConsoleInput.readAmount(sc, "Enter amount to withdraw: ");
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        if (account.withdraw(amount)) {
            System.out.printf("Withdrawal successful. New balance: Rs.%.2f%n", account.getBalance());
        } else {
            System.out.println("Insufficient Funds");
        }
    }
}
