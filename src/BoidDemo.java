import java.util.Scanner;

public class BoidDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Determine Rendering Method
        System.out.println("Render using javaFX?(y/n): ");
        String input = sc.next();

        if(input.equalsIgnoreCase("y")) {
            // run program and render using additional JavaFX thread alongside the main thread
            BoidController controller = new BoidController(true);
        } else {
            // run program without any rendering
            BoidController controller = new BoidController(false);
        }
        sc.close();
    }
}