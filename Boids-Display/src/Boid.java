import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Boid {
    private final Screen screen;

    private double x,y,fov, viewDistance, direction; // x position and y position
    private double prevX, prevY;

    private int XSectorIndex, YSectorIndex;

    private ArrayList<Boid> boidsInView;

    // Boid Constructors

    public Boid(Screen screen, double viewDistance, double fov, double separation, double alignment, double cohesion) {
        this.screen = screen;
        setViewDistance(viewDistance);
        setFov(fov);
        setX(Math.random() * screen.getSizeX());
        setY(Math.random() * screen.getSizeY());
    }

    // Boid functions

    public void updateSector(ArrayList<Boid>[][] sectors) {
        sectors[XSectorIndex][YSectorIndex].remove(this);
        int newXSectorIndex = (int)(x / ((double) screen.getSizeX()/sectors.length));
        int newYSectorIndex = (int)(y / ((double) screen.getSizeY() /sectors.length));
        sectors[newXSectorIndex][newYSectorIndex].add(this);
        XSectorIndex = newXSectorIndex;
        YSectorIndex = newYSectorIndex;
    }

    public void printBoid() {
        System.out.println("--------------------");
        System.out.println("Sector [" + XSectorIndex + "][" + YSectorIndex + "]");
        System.out.println("Direction: " + getDirection());
        System.out.println("X position: " + getX());
        System.out.println("Y position: " + getY());
        System.out.println("--------------------");
    }

    public synchronized void changePosition(double changeX, double changeY) {
        prevX = x;
        prevY = y;
        x += changeX;
        y += changeY;
        if(x > screen.getSizeX())
            x -= screen.getSizeX();
        if(x < 0)
            x += screen.getSizeX();
        if(y > screen.getSizeY())
            y -= screen.getSizeY();
        if(y < 0)
            y += screen.getSizeY();

        updateDirection();
    }

    public synchronized void updateDirection() {
        double changeX = x - prevX;
        double changeY = y - prevY;
        direction = Math.toDegrees(Math.atan2(changeY, changeX));
    }

    // Boid getters and setters
    public double getViewDistance() {return viewDistance;}
    public double getX() {return x;}
    public double getY() {return y;}
    public synchronized double getDirection() {return direction;}
    public double getFov() {return fov;}
    public double getPrevX() {return prevX;}
    public double getPrevY() {return prevY;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setFov(double fov) {this.fov = fov;}
    public void setViewDistance(double viewDistance) {this.viewDistance = viewDistance;}
}