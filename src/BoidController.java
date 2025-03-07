import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class BoidController {
    // Screen information
    private static int screenWidth;
    private static int screenHeight;
    private static ArrayList<Boid>[][] sectors;
    private static double totalTime = 0;

    // Boid information (Consistent for all boids)
    private static double boidViewDistance; // how far in pixels the boids can see
    private static double boidFieldOfView; // the boids field of view in degrees
    private static final ArrayList<Boid> boids = new ArrayList<Boid>(); // a list of every boid

    public BoidController(boolean usingJavaFX) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of boids: ");
        // Screen information
        int numBoids = sc.nextInt();

        System.out.println("Enter the width of the screen: ");
        screenWidth = sc.nextInt();

        System.out.println("Enter the height of the screen: ");
        screenHeight = sc.nextInt();

        System.out.println("Enter boids view distance: ");
        boidViewDistance = sc.nextDouble();

        System.out.println("Enter boids field of view in degrees: ");
        boidFieldOfView = sc.nextDouble();

        System.out.println("(Temporary) Enter movement speed: ");
        // Temporary variables that will be replaced later by methods
        double speed = sc.nextDouble();

        // the time in nanoseconds of the previous update
        long previousTime = System.nanoTime();

        // Setup before entering the update loop
        createSectors();
        createBoids(numBoids);

        // Timer for debugging, will be removed at some point.
        double timer = 0;

        // Starts the RenderFX thread if usingJavaFX
        if(usingJavaFX) {
            RenderFX.launchScreen();
        }

        printEmptySectors();

        // Main Update loop
        boolean running = true;
        while(running) {
            long currentTime = System.nanoTime();
            // the time since the last update in seconds
            double deltaTime = (System.nanoTime() - previousTime) / 1000000000.0;
            previousTime = currentTime;

            RenderFX.setBoidController(this);

            for(Boid boid : boids) {
                boid.updateSector(sectors);
                boid.changePosition(Math.sin((totalTime / 2) * Math.PI) * speed * deltaTime, Math.cos((totalTime / 2) * Math.PI) * speed * deltaTime);
                //TODO: update boidsInVision array to accurately contain each boid in its vision (Only checks adjacent sectors for nearby boids)
            }

            if(timer >= 10){
                printRuntime();
                printBoids();
                timer = 0;
            }
            totalTime += deltaTime;
            timer += deltaTime;
        }
    }

    // creates sectors with the amount of sectors being determined by the screens width and height divided by the vision range of the boids
    @SuppressWarnings("unchecked") // Unchecked ArrayList type is handled by iterating through the 2D ArrayList array and setting each element equal to a new ArrayList<Boid> (I just wanted the yellow squiggles to go away)
    public void createSectors() {
        sectors = new ArrayList[(int)(screenWidth/boidViewDistance)][(int)(screenHeight/boidViewDistance)];
        for(int i = 0; i < sectors.length; i++) {
            for(int j = 0; j < sectors[i].length; j++) {
                sectors[i][j] = new ArrayList<Boid>();
            }
        }
    }

    // creates num amount of boids and adds them to the boids ArrayList
    public void createBoids(int num) {
        for(int i = 0; i < num; i++) {
            boids.add(new Boid(this, boidViewDistance, boidFieldOfView));
        }
    }

    // prints every boid in the boids ArrayList
    public void printBoids() {
        System.out.println("Boids: \n--------------------");
        for(int i = 0; i < boids.size(); i++) {
            System.out.println("Boid " + i);
            System.out.println(boids.get(i) + "--------------------");
        }
    }

    // prints total runtime in seconds
    public void printRuntime() {
        System.out.println("Runtime: " + totalTime +"\n");
    }

    // prints the 2D array of sectors without any information about what each sector contains.
    public void printEmptySectors() {
        for(int i = 0; i < sectors.length; i++) {
            for(int j = 0; j < sectors[i].length; j++) {
                System.out.print("[" + i + "][" + j + "]   ");
            }
            System.out.println();
        }
    }

    // Getters and Setters
    public synchronized int getScreenWidth() {return screenWidth;}
    public synchronized int getScreenHeight() {return screenHeight;}
    public synchronized ArrayList<Boid> getBoids() {return new ArrayList<Boid>(boids);}
}