import java.util.Scanner;

public class HistoryAction implements MenuAction {

    @Override
    public String label() {
        return "Transaction History";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        System.out.println("\n--- Transaction History ---");
        if (account.getHistory().isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : account.getHistory()) {
            System.out.println(t);
        }
    }
}
