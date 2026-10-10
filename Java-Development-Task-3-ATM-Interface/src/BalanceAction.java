import java.util.Scanner;

public class BalanceAction implements MenuAction {

    @Override
    public String label() {
        return "Check Balance";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        System.out.printf("Current balance: Rs.%.2f%n", account.getBalance());
    }
}