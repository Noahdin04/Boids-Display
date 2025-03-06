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
    private static final Object lock = new Object();

    private static BoidController controller;

    // final variables that only need to be retrieved once
    private static int screenWidth;
    private static int screenHeight;

    // variables that will need to change every update to display current boids accurately
    ArrayList<Boid> boids;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // wait for main thread to supply this thread with the BoidController
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

        screenWidth = controller.getScreenWidth();
        screenHeight = controller.getScreenHeight();

        Pane pane = new Pane();
        Scene scene = new Scene(pane, controller.getScreenWidth(), controller.getScreenHeight());

        ArrayList<Arc> visionArcs = new ArrayList<Arc>();
        ArrayList<Line> visionLines = new ArrayList<Line>();
        ArrayList<Line> visionLines2 = new ArrayList<Line>();
        ArrayList<Polygon> visionPolygons = new ArrayList<Polygon>();

        for(int i = 0; i < controller.getBoids().size(); i++) {

            Color fillColor = new Color(Math.random(), Math.random(), Math.random(), 0.25);

            Arc arc = new Arc();
            arc.setFill(fillColor);
            arc.setStroke(Color.BLACK);
            visionArcs.add(arc);
            pane.getChildren().add(arc);

            Line line = new Line();
            visionLines.add(line);
            pane.getChildren().add(line);

            Line line2 = new Line();
            visionLines2.add(line2);
            pane.getChildren().add(line2);

            Polygon polygon = new Polygon();
            polygon.setFill(fillColor);
            visionPolygons.add(polygon);
            pane.getChildren().add(polygon);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // JavaFX Update loop
                boids = controller.getBoids();

                for(int i = 0; i < boids.size(); i++) {
                    double X = boids.get(i).getX();
                    double Y = screenHeight - boids.get(i).getY();

                    double lineEndX = X + boids.get(i).getViewDistance() * Math.cos(Math.toRadians(boids.get(i).getDirection() - (boids.get(i).getFieldOfView()/2)));
                    double lineEndY = Y - boids.get(i).getViewDistance() * Math.sin(Math.toRadians(boids.get(i).getDirection() - (boids.get(i).getFieldOfView()/2)));
                    double line2EndX = X + boids.get(i).getViewDistance() * Math.cos(Math.toRadians(boids.get(i).getDirection() + (boids.get(i).getFieldOfView()/2)));
                    double line2EndY = Y - boids.get(i).getViewDistance() * Math.sin(Math.toRadians(boids.get(i).getDirection() + (boids.get(i).getFieldOfView()/2)));

                    visionArcs.get(i).setCenterX(X);
                    visionArcs.get(i).setCenterY(Y);
                    visionArcs.get(i).setRadiusX(boids.get(i).getViewDistance());
                    visionArcs.get(i).setRadiusY(boids.get(i).getViewDistance());
                    visionArcs.get(i).setStartAngle(boids.get(i).getDirection() - (boids.get(i).getFieldOfView()/2));
                    visionArcs.get(i).setLength(boids.get(i).getFieldOfView());

                    visionLines.get(i).setStartX(X);
                    visionLines.get(i).setStartY(Y);
                    visionLines.get(i).setEndX(lineEndX);
                    visionLines.get(i).setEndY(lineEndY);

                    visionLines2.get(i).setStartX(X);
                    visionLines2.get(i).setStartY(Y);
                    visionLines2.get(i).setEndX(line2EndX);
                    visionLines2.get(i).setEndY(line2EndY);

                    visionPolygons.get(i).getPoints().clear();
                    visionPolygons.get(i).getPoints().addAll(X,Y,lineEndX,lineEndY,line2EndX,line2EndY);

                    //TODO: next step is to calculate the boid body points
                }
            }
        };timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Boids Display");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

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

    public synchronized static void setBoidController(BoidController updatedController) {
        controller = updatedController;
    }
}
