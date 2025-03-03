import java.util.ArrayList;

public class Screen {
    int sizeX;
    int sizeY;
    long previousTime;

    double speed;

    double screenTime = 0;

    ArrayList[][] sectors;

    ArrayList<Boid> boids;

    // Screen Constructors

    public Screen(int sizeX, int sizeY, double speed) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.speed = speed;
        previousTime = System.nanoTime();
        boids = new ArrayList<Boid>();
    }

    // Screen functions

    public void Update(double deltaTime) {
        for (Boid boid : boids) {
            boid.updateSector(sectors);
            boid.changePosition(speed * deltaTime,speed * deltaTime);
        }

        // updates total time that the screen has been running (Max screen time is 1.7976931348623157E+308 seconds AKA a really, REALLY, long time)
        screenTime += deltaTime;
        if(screenTime == Double.POSITIVE_INFINITY) {
            screenTime = 0;
        }
    }

    public void addBoid(double viewDistance, double fov, double separation, double alignment, double cohesion) {
        Boid boid = new Boid(this, viewDistance, fov, separation, alignment, cohesion);
        boids.add(boid);
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