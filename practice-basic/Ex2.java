import java.util.*;


public class Ex2 {
    public static void main (String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the electricity degree in this month: ");
        int elecDegree = sc.nextInt();
        if (elecDegree <= 100) {
            System.out.println(elecDegree * 1000);
        }
        else if (elecDegree <= 200) {
            System.out.println(100 * 1000 + (elecDegree - 100) * 1500);
        }
        else {
            System.out.println(100 * 1000 + 100 * 1500 + (elecDegree - 200) * 2000);
        }
    }
}
