import java.util.ArrayList;

public class Screen {
    int sizeX;
    int sizeY;
    long previousTime;

    ArrayList<Boid>[][] sectors;

    ArrayList<Boid> boids;

    // Screen Constructors

    public Screen() {
        sizeX = 400;
        sizeY = 400;
        previousTime = System.nanoTime();
        boids = new ArrayList<Boid>();

    }

    public Screen(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        previousTime = System.nanoTime();
        boids = new ArrayList<Boid>();
    }

    // Screen functions

    public void Update(double deltaTime) {
        for(int i = 0; i < boids.size(); i++) {
            // code that handles boid updates
            boids.get(i).updateSector(sectors);
            boids.get(i).changeX(500 * deltaTime);
            boids.get(i).changeY(500 * deltaTime);
            boids.get(i).updateDirection();
        }
    }

    public void addBoid(double viewDistance, double fov, double separation, double alignment, double cohesion) {
        Boid boid = new Boid(this, viewDistance, fov, separation, alignment, cohesion);
    }

    public void printBoids() {
        for(int i = 0; i < boids.size(); i++) {
            System.out.println("Boid " + i);
            boids.get(i).printBoid();
        }
    }

    public void createSectors(double viewDistance) {
        sectors = new ArrayList[(int)(sizeX/viewDistance)][(int)(sizeY/viewDistance)];
        for(int i = 0; i < sectors.length; i++) {
            for(int j = 0; j < sectors[i].length; j++) {
                sectors[i][j] = new ArrayList<Boid>();
                System.out.print("[" + i + "][" + j + "] , ");
            }
            System.out.println();
        }
    }

    public void printSectors() {
        int tempNumBoids = 0;
        for(int i = 0; i < sectors.length; i++) {
            for(int j = 0; j < sectors[i].length; j++) {
                tempNumBoids += sectors[i][j].size();
                System.out.println("[" + i + "][" + j + "] size: " + sectors[i][j].size());
            }
        }
        System.out.println("Total boids: " + tempNumBoids);
    }

    // Screen getters and setters
    public int getSizeX() {return sizeX;}
    public int getSizeY() {return sizeY;}
    public ArrayList<Boid> getBoids() {return boids;}
    public ArrayList[][] getSectors() {return sectors;}
}