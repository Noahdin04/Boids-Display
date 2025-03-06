import java.util.ArrayList;

public class Boid {
    // Parent references
    BoidController controller;

    // Boid Attributes
    private double x, y, direction, fieldOfView;
    private double viewDistance;
    private ArrayList<Boid> boidsInView;

    // Boid indices
    private int sectorIndexX, sectorIndexY;

    // Boid Constructor(s)
    public Boid(BoidController controller, double viewDistance, double fieldOfView) {
        this.controller = controller;
        x = Math.random() * controller.getScreenWidth();
        y = Math.random() * controller.getScreenHeight();
        this.viewDistance = viewDistance;
        this.fieldOfView = fieldOfView;
    }

    public void updateSector(ArrayList<Boid>[][] sectors) { // checks and update which sector contains this boid
        int newSectorIndexX = (int)(x / ((double) controller.getScreenWidth() / sectors.length));
        int newSectorIndexY = (int)(y / ((double) controller.getScreenHeight() / sectors[0].length));
        if(newSectorIndexX != sectorIndexX || newSectorIndexY != sectorIndexY) {
            sectors[sectorIndexX][sectorIndexY].remove(this);
            sectors[newSectorIndexX][newSectorIndexY].add(this);
            sectorIndexX = newSectorIndexX;
            sectorIndexY = newSectorIndexY;
        }
    }

    public synchronized void changePosition(double changeX, double changeY) {
        double previousX = x;
        double previousY = y;
        x += changeX;
        y += changeY;

        // Handles the condition where either x or y gets out of bounds of the screen
        if(x > controller.getScreenWidth())
            x -= controller.getScreenWidth();
        if(y > controller.getScreenHeight())
            y -= controller.getScreenHeight();
        if(x < 0)
            x += controller.getScreenWidth();
        if(y < 0)
            y += controller.getScreenHeight();

        // updates the direction the boid is facing based off of the change in x and y
        double changeInX = x - previousX;
        double changeInY = y - previousY;
        direction = Math.toDegrees(Math.atan2(changeInY, changeInX));
    }

    public void findBoidsInVision() {
        // look in nearby sectors for boids that might be in view and then check if they are in view
    }

    @Override
    public String toString() {
        String output;
        output = "Sector [" + sectorIndexX + "][" +sectorIndexY + "]\n" +
                 "X position: " + x + "\n" +
                 "Y position: " + y + "\n" +
                 "Direction: " + direction + "\n" +
                 "--------------------";
        return output;
    }

    // Getters and Setters
    public synchronized double getX() {return x;}
    public synchronized double getY() {return y;}
    public synchronized double getDirection() {return direction;}
    public synchronized double getViewDistance() {return viewDistance;}
    public synchronized double getFieldOfView() {return fieldOfView;}
}