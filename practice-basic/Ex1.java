import java.util.*;

public class Ex1 {
    private static final int attempt = 5;
    public static void main(String[] args) {
        // Choose the right number
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the right number: ");
        int correctNumber = sc.nextInt();

        int chance = 1;
        while (true) {
            int numberEnter = sc.nextInt();
            if (numberEnter == correctNumber) {
                System.out.println("correct number");
                return;
            }
            if (chance == attempt && numberEnter != correctNumber) {
                System.out.println("Invalid number");
                return;
            }
            else {
                chance ++;
            }
        }
    }
}