import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScreenFX extends Application {
    static boolean hasLaunched = false;

    CopyOnWriteArrayList<Boid> threadSafeBoids = new CopyOnWriteArrayList<Boid>();

    public static ArrayList<Boid> boids;
    public static Screen screen;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, screen.getSizeX(), screen.getSizeY());

        ArrayList<Circle> boidCircles = new ArrayList<Circle>();
        ArrayList<Line> visionLines = new ArrayList<Line>();
        ArrayList<Line> visionLines2 = new ArrayList<Line>();

        for(int i = 0; i < boids.size(); i++) {
            Circle circle = new Circle();
            circle.setFill(new Color(Math.random(), Math.random(), Math.random(), 0.1));
            circle.setStroke(Color.BLACK);
            boidCircles.add(circle);
            pane.getChildren().add(circle);

            Line line = new Line();
            visionLines.add(line);
            pane.getChildren().add(line);

            Line line2 = new Line();
            visionLines2.add(line2);
            pane.getChildren().add(line2);
        }

        // Acts as the update loop for the JavaFX screen which runs every frame
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for(int i = 0; i < boids.size(); i++) {
                    double tempX = boids.get(i).getX();
                    double tempY = screen.getSizeY() - boids.get(i).getY();
                    boidCircles.get(i).setCenterX(tempX);
                    boidCircles.get(i).setCenterY(tempY); // take screen height - y position since JavaFX hav an inverted Y axis
                    boidCircles.get(i).setRadius(boids.get(i).getViewDistance());

//                    System.out.println(boids.getFirst().getDirection());

                    visionLines.get(i).setStartX(tempX);
                    visionLines.get(i).setStartY(tempY);
                    visionLines.get(i).setEndX(tempX + boids.get(i).getViewDistance() * Math.cos(Math.toRadians(boids.get(i).getDirection() - (boids.get(i).getFov()/2))));
                    visionLines.get(i).setEndY(tempY + boids.get(i).getViewDistance() * Math.sin(Math.toRadians(boids.get(i).getDirection() - (boids.get(i).getFov()/2))));

                    visionLines2.get(i).setStartX(tempX);
                    visionLines2.get(i).setStartY(tempY);
                    visionLines2.get(i).setEndX(tempX + boids.get(i).getViewDistance() * Math.cos(Math.toRadians(boids.get(i).getDirection() + (boids.get(i).getFov()/2))));
                    visionLines2.get(i).setEndY(tempY + boids.get(i).getViewDistance() * Math.sin(Math.toRadians(boids.get(i).getDirection() + (boids.get(i).getFov()/2))));
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