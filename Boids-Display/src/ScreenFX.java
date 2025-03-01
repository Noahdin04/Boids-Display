import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScreenFX extends Application {
    static boolean hasLaunched = false;

    public static ArrayList<Boid> boids;
    public static Screen screen;

    public double boidSideLength;
    public double boidWidth;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, screen.getSizeX(), screen.getSizeY());

        ArrayList<Arc> visionArcs = new ArrayList<Arc>();
        ArrayList<Line> visionLines = new ArrayList<Line>();
        ArrayList<Line> visionLines2 = new ArrayList<Line>();
        for(int i = 0; i < boids.size(); i++) {
            double x = boids.get(i).getX();
            double y = boids.get(i).getY();
            double r = boids.get(i).getViewDistance();
            Arc visionArc = new Arc();
            visionArc.setFill(Color.TRANSPARENT);
            visionArc.setStroke(Color.BLACK);
            visionArcs.add(visionArc);
            pane.getChildren().add(visionArc);

            Line visionLine = new Line();
            visionLines.add(visionLine);
            pane.getChildren().add(visionLine);

            Line visionLine2 = new Line();
            visionLines2.add(visionLine2);
            pane.getChildren().add(visionLine2);
        }

//        for(int i = 0; i < boids.size(); i++) {
//
//            Polygon polygon = new Polygon();
//            double x = boids.get(i).getX();
//            double y = boids.get(i).getY();
//            double x2 = boids.get(i).getX();
//            double y2 = boids.get(i).getY();
//            double x3 = boids.get(i).getX();
//            double y3 = boids.get(i).getY();
//            polygon.getPoints().addAll(x,y,x2,y2,x3,y3);
//        }

        // Acts as the update loop for the JavaFX screen which runs every frame
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for(int i = 0; i < boids.size(); i++) {

                    // Updates the boids Field of View
                    visionArcs.get(i).setRadiusX(boids.get(i).getViewDistance());
                    visionArcs.get(i).setRadiusY(boids.get(i).getViewDistance());
                    visionArcs.get(i).setCenterX(boids.get(i).getX());
                    visionArcs.get(i).setCenterY(boids.get(i).getY());
                    visionArcs.get(i).setStartAngle(-boids.get(i).getDirection() + boids.get(i).getFov()/2);
                    visionArcs.get(i).setLength(-boids.get(i).getFov());

                    // updates the position of the lines connecting the ends of the vision arc to the position of the boid
                    visionLines.get(i).setStartX(boids.get(i).getX());
                    visionLines.get(i).setStartY(boids.get(i).getY());
                    visionLines2.get(i).setStartX(boids.get(i).getX());
                    visionLines2.get(i).setStartY(boids.get(i).getY());
                    double changeX = boids.get(i).getX() - boids.get(i).getPrevX();
                    double changeY = boids.get(i).getY() - boids.get(i).getPrevY();
                    if(changeX >= 0 && changeY < 0 || changeX < 0 && changeY >= 0)
                    {
                        visionLines.get(i).setEndX(visionArcs.get(i).getCenterX() + visionArcs.get(i).getRadiusX() * -Math.cos(Math.toRadians(visionArcs.get(i).getStartAngle())));
                        visionLines.get(i).setEndY(visionArcs.get(i).getCenterY() + visionArcs.get(i).getRadiusY() * -Math.sin(Math.toRadians(visionArcs.get(i).getStartAngle())));
                        visionLines2.get(i).setEndX(visionArcs.get(i).getCenterX() + visionArcs.get(i).getRadiusX() * -Math.cos(Math.toRadians(visionArcs.get(i).getStartAngle() - visionArcs.get(i).getLength())));
                        visionLines2.get(i).setEndY(visionArcs.get(i).getCenterY() + visionArcs.get(i).getRadiusY() * -Math.sin(Math.toRadians(visionArcs.get(i).getStartAngle() - visionArcs.get(i).getLength())));
                    }
                    else {
                        visionLines.get(i).setEndX(visionArcs.get(i).getCenterX() + visionArcs.get(i).getRadiusX() * Math.cos(Math.toRadians(visionArcs.get(i).getStartAngle())));
                        visionLines.get(i).setEndY(visionArcs.get(i).getCenterY() + visionArcs.get(i).getRadiusY() * Math.sin(Math.toRadians(visionArcs.get(i).getStartAngle())));
                        visionLines2.get(i).setEndX(visionArcs.get(i).getCenterX() + visionArcs.get(i).getRadiusX() * Math.cos(Math.toRadians(visionArcs.get(i).getStartAngle() - visionArcs.get(i).getLength())));
                        visionLines2.get(i).setEndY(visionArcs.get(i).getCenterY() + visionArcs.get(i).getRadiusY() * Math.sin(Math.toRadians(visionArcs.get(i).getStartAngle() - visionArcs.get(i).getLength())));
                    }
                }
            }
        };timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Boids Display");
        primaryStage.show();
    }

    public static void launchScreen() {
        if(!hasLaunched) {
            hasLaunched = true;
            new Thread(() -> Application.launch(ScreenFX.class)).start();
        } else {
            Platform.runLater(() -> {
                System.out.println("ScreenFX is already running");
            });
        }
    }
}