import java.util.Scanner;

public class QuitAction implements MenuAction {

    @Override
    public String label() {
        return "Quit";
    }

    @Override
    public void execute(Account account, BankOperations bank, Scanner sc) {
        System.out.println("Thank you for banking with us, " + account.getHolderName() + ". Goodbye!");
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
