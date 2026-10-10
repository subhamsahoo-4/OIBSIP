import java.util.Scanner;

/** Single-purpose helper: safely read a positive amount from the console. */
public final class ConsoleInput {

    private ConsoleInput() {}

    /** Returns the parsed amount, or -1 if the input was blank, non-numeric, or not positive. */
    public static double readAmount(Scanner sc, String prompt) {
        System.out.print(prompt);
        try {
            double amount = Double.parseDouble(sc.nextLine().trim());
            return amount > 0 ? amount : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
