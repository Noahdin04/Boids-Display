import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class RenderFX extends Application {
    private static boolean hasLaunched = false;

    // Object lock that is used to force the thread to wait until receiving an instance of BoidController
    private static final Object lock = new Object();

    private static BoidController controller;

    // variables that only need to be retrieved once
    private static int screenWidth;
    private static int screenHeight;

    // variables that will need to change every update to display each boid accurately
    ArrayList<Boid> boids;

    // acts as the main function for the JavaFX thread
    @Override
    public void start(Stage primaryStage) throws Exception {
        // wait for the main thread to supply this thread with an instance of BoidController
        System.out.println("Waiting for BoidController");
        synchronized (lock) {
            while(controller == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Received BoidController");
        }

        // set the variables that only need to be retrieved once
        screenWidth = controller.getScreenWidth();
        screenHeight = controller.getScreenHeight();

        // create the JavaFX elements for rendering the screen
        Pane pane = new Pane();
        Scene scene = new Scene(pane, controller.getScreenWidth(), controller.getScreenHeight());

        // ArrayLists containing the JavaFX objects to be rendered on screen
        ArrayList<Arc> visionArcs = new ArrayList<Arc>();
        ArrayList<Line> visionLines = new ArrayList<Line>();
        ArrayList<Line> visionLines2 = new ArrayList<Line>();
        ArrayList<Polygon> visionPolygons = new ArrayList<Polygon>();
        ArrayList<Polygon> boidBodies = new ArrayList<Polygon>();

        // create the JavaFX objects for each boid to be shown on screen
        for(int i = 0; i < controller.getBoids().size(); i++) {
            Color fillColor = new Color(Math.random(), Math.random(), Math.random(), 0.25); // fill color representing vision

            // Vision Arc object creation
            Arc arc = new Arc();
            arc.setFill(fillColor);
            arc.setStroke(Color.BLACK);
            visionArcs.add(arc);
            pane.getChildren().add(arc);

            // Vision Line object creation
            Line line = new Line();
            visionLines.add(line);
            pane.getChildren().add(line);

            // Seconds Vision Line object creation
            Line line2 = new Line();
            visionLines2.add(line2);
            pane.getChildren().add(line2);

            // Vision Polygon object creation
            Polygon polygon = new Polygon();
            polygon.setFill(fillColor);
            polygon.setStroke(Color.TRANSPARENT);
            visionPolygons.add(polygon);
            pane.getChildren().add(polygon);

            // Boid Body object creation
            Polygon boidPolygon = new Polygon();
            boidPolygon.setFill(Color.RED);
            boidPolygon.setStroke(Color.BLACK);
            boidBodies.add(boidPolygon);
            pane.getChildren().add(boidPolygon);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // JavaFX Update loop

                // Update the boids array with current boid information
                boids = controller.getBoids();

                // update the JavaFX objects for each boid
                for(int i = 0; i < boids.size(); i++) {
                    double X = boids.get(i).getX();
                    double Y = screenHeight - boids.get(i).getY();

                    // finds the distance for both X and Y values to both the start and end points of the vision arc
                    double xDistanceToPoint2 = boids.get(i).getViewDistance() * Math.cos(Math.toRadians(boids.get(i).getDirection() - (boids.get(i).getFieldOfView()/2)));
                    double yDistanceToPoint2 = boids.get(i).getViewDistance() * Math.sin(Math.toRadians(boids.get(i).getDirection() - (boids.get(i).getFieldOfView()/2)));
                    double xDistanceToPoint3 = boids.get(i).getViewDistance() * Math.cos(Math.toRadians(boids.get(i).getDirection() + (boids.get(i).getFieldOfView()/2)));
                    double yDistanceToPoint3 = boids.get(i).getViewDistance() * Math.sin(Math.toRadians(boids.get(i).getDirection() + (boids.get(i).getFieldOfView()/2)));

                    // draws the arcs that represent how far the boid can see
                    visionArcs.get(i).setCenterX(X);
                    visionArcs.get(i).setCenterY(Y);
                    visionArcs.get(i).setRadiusX(boids.get(i).getViewDistance());
                    visionArcs.get(i).setRadiusY(boids.get(i).getViewDistance());
                    visionArcs.get(i).setStartAngle(boids.get(i).getDirection() - (boids.get(i).getFieldOfView()/2));
                    visionArcs.get(i).setLength(boids.get(i).getFieldOfView());

                    // draws a line to the start of the vision arc representing one edge of its vision
                    visionLines.get(i).setStartX(X);
                    visionLines.get(i).setStartY(Y);
                    visionLines.get(i).setEndX(X + xDistanceToPoint2);
                    visionLines.get(i).setEndY(Y - yDistanceToPoint2);

                    // draws a line to the end of the vision arc representing the other edge of its vision
                    visionLines2.get(i).setStartX(X);
                    visionLines2.get(i).setStartY(Y);
                    visionLines2.get(i).setEndX(X + xDistanceToPoint3);
                    visionLines2.get(i).setEndY(Y - yDistanceToPoint3);

                    // fills in the area between the starting point of vision and the arc so that the fill color accurately represents its vision
                    visionPolygons.get(i).getPoints().clear();
                    visionPolygons.get(i).getPoints().addAll(X,Y,X + xDistanceToPoint2,Y - yDistanceToPoint2,X + xDistanceToPoint3,Y - yDistanceToPoint3);

                    // calculates boid body points that represent the actual boid (This currently is dependent on FOV and View distance which should probably be changed)
                    boidBodies.get(i).getPoints().clear();
                    boidBodies.get(i).getPoints().addAll(X,Y,X - xDistanceToPoint2/5, Y + yDistanceToPoint2/5, X - xDistanceToPoint3/5, Y + yDistanceToPoint3/5);
                }
            }
        };timer.start();

        // finishes setting up the JavaFX screen
        primaryStage.setScene(scene);
        primaryStage.setTitle("Boids Display");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // closes both the JavaFX thread and the main thread when the JavaFX screen is closed
        System.exit(0);
    }

    // Starts the RenderFX thread
    public static void launchScreen() {
        if(!hasLaunched) {
            hasLaunched = true;
            new Thread(() -> Application.launch(RenderFX.class)).start();
        } else {
            Platform.runLater(() -> {
                System.out.println("RenderFX already launched");
            });
        }
    }

    // Getters and Setters
    public synchronized static void setBoidController(BoidController updatedController) {
        controller = updatedController;
    }
}