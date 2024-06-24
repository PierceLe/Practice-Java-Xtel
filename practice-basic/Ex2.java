import java.util.*;


public class Ex2 {
    public static void main (String args[]) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhập số điện đã dùng trong tháng: ");
        int usage = scanner.nextInt();
        
        int bill = calculateElectricityBill(usage);
        
        System.out.println("Số tiền điện phải trả: " + bill + " VND");
    }
    
    public static int calculateElectricityBill(int usage) {
        int bill = 0;
        
        if (usage <= 100) {
            bill = usage * 1000;
        } else if (usage <= 150) {
            bill = 100 * 1000 + (usage - 100) * 1500;
        } else {
            bill = 100 * 1000 + 50 * 1500 + (usage - 150) * 2000;
        }
        
        return bill;
    }
}
