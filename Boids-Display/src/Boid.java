import java.util.ArrayList;

public class Boid {
    private Screen screen;

    private double x,y; // x position and y position
    private double vx,vy; // x velocity and y velocity
    private double ax,ay; // x acceleration and y acceleration

    private double separation, alignment, cohesion; // determines the strength of separation, alignment, and cohesion from 0-1

    private double fov, viewDistance, direction;

    private int XSectorIndex, YSectorIndex;

    private ArrayList<Boid> boidsInView;

    // Boid Constructors

    public Boid(Screen screen, double viewDistance, double fov, double separation, double alignment, double cohesion) {
        this.screen = screen;
        screen.getBoids().add(this);

        setViewDistance(viewDistance);
        setFov(fov);

        setSeparation(separation);
        setAlignment(alignment);
        setCohesion(cohesion);

        setX(Math.random() * screen.getSizeX());
        setY(Math.random() * screen.getSizeY());
        setVX(Math.random());
        setVY(Math.random());
        setAX(0);
        setAY(0);
    }

    // Boid functions

    public void updateSector(ArrayList<Boid>[][] sectors) {
        sectors[XSectorIndex][YSectorIndex].remove(this);
        int newXSectorIndex = (int)(x / viewDistance);
        int newYSectorIndex = (int)(y / viewDistance);
        sectors[newXSectorIndex][newYSectorIndex].add(this);
        XSectorIndex = newXSectorIndex;
        YSectorIndex = newYSectorIndex;
    }

    public void printBoid() {
        System.out.println("--------------------");
        System.out.println("Sector [" + XSectorIndex + "][" + YSectorIndex + "]");
        System.out.println("X position: " + x);
        System.out.println("Y position: " + y);
        System.out.println("X velocity: " + vx);
        System.out.println("Y velocity: " + vy);
        System.out.println("X acceleration: " + ax);
        System.out.println("Y acceleration: " + ay);
        System.out.println("Separation: " + separation);
        System.out.println("Alignment: " + alignment);
        System.out.println("Cohesion: " + cohesion);
        System.out.println("--------------------");
    }

    public void changeX(double change) {
        x += change;
        if(x > screen.getSizeX())
            x = 0;
    }

    public void changeY(double change) {
        y += change;
        if(y > screen.getSizeY())
            y = 0;
    }

    // Boid getters and setters
    public double getViewDistance() {return viewDistance;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setVX(double vx) {this.vx = vx;}
    public void setVY(double vy) {this.vy = vy;}
    public void setAX(double ax) {this.ax = ax;}
    public void setAY(double ay) {this.ay = ay;}
    public void setFov(double fov) {this.fov = fov;}
    public void setViewDistance(double viewDistance) {this.viewDistance = viewDistance;}
    public void setSeparation(double separation) {this.separation = separation;}
    public void setAlignment(double alignment) {this.alignment = alignment;}
    public void setCohesion(double cohesion) {this.cohesion = cohesion;}
}