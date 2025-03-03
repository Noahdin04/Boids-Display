import javafx.application.Application;

import java.util.Scanner;

public class boidDemo{
    public static double speed = 200;

    public static int numBoids;
    public static int sizeX;
    public static int sizeY;

    public static double viewDistance; // boids viewing distance in pixels, also determines sector size
    public static double fov;

    public static double separation;
    public static double alignment;
    public static double cohesion;

    public static boolean render;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // Manually set variables before program runs

        System.out.println("Enter the number of boids: ");
        numBoids = sc.nextInt();

        System.out.println("Enter the width of the screen: ");
        sizeX = sc.nextInt();

        System.out.println("Enter the height of the screen: ");
        sizeY = sc.nextInt();

        System.out.println("Enter boids view distance: ");
        viewDistance = sc.nextDouble();

        System.out.println("Enter boids fov: ");
        fov = sc.nextDouble();

//        System.out.println("Enter the separation strength (0.0-1.0): ");
//        separation = sc.nextDouble();
//
//        System.out.println("Enter the alignment strength (0.0-1.0): ");
//        alignment = sc.nextDouble();
//
//        System.out.println("Enter the cohesion strength (0.0-1.0): ");
//        cohesion = sc.nextDouble();

        separation = 1;
        alignment = 1;
        cohesion = 1;

        System.out.println("Render boids?(y/n): ");
        String response = sc.next();
        if(response.equalsIgnoreCase("y")) {
            render = true;
        } else if(response.equalsIgnoreCase("n")) {
            render = false;
        } else {
            System.out.println("Invalid input");
        }

        // prepares the update loop
        boolean running = true;
        long previousTime = System.nanoTime();

        // Screen initialization
        Screen screen = new Screen(sizeX, sizeY, speed);
        screen.createSectors(viewDistance);

        // adds boids to the screen
        for(int i = 0; i < numBoids; i++) {
            screen.addBoid(viewDistance, fov, separation, alignment, cohesion);
        }

        for(int i = 0; i < screen.getBoids().size(); i++) {
            // code that handles boid updates
            screen.getBoids().get(i).updateSector(screen.getSectors());
        }

        if(render) {
            ScreenFX.screen = screen;
            ScreenFX.boids = ScreenFX.screen.getBoids();
            ScreenFX.launchScreen();
        }

        double timer = 0;
        // prints starting information for each boid
        while(running) {
            long currentTime = System.nanoTime();   // gets current time in nanoseconds
            double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;  // evaluates deltaTime in nanoseconds and converts to seconds

            screen.Update(deltaTime);   // updates the screen

            previousTime = currentTime; // sets the previous time to the current time to prepare for the next update call

            if(render) {
                ScreenFX.boids = screen.getBoids();
                ScreenFX.screen = screen;
            }

            System.out.println(screen.getBoids().getFirst().getDirection());

            timer += deltaTime;
            if(timer >= 30) {
                timer = 0;
                screen.printBoids();
            }
        }
    }
}