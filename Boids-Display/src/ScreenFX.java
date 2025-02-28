import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        System.out.println(boids.size());
        System.out.println(screen);
        Pane pane = new Pane();
        Scene scene = new Scene(pane, screen.getSizeX(), screen.getSizeY());

        ArrayList<Circle> boidCircles = new ArrayList<>();
        for(int i = 0; i < boids.size(); i++) {
            Circle circle = new Circle();
            circle.setFill(new Color(Math.random(),Math.random(),Math.random(),0.1));
            circle.setStroke(Color.BLACK);
            circle.setCenterX(boids.get(i).getX());
            circle.setCenterY(boids.get(i).getY());
            circle.setRadius(boids.get(i).getViewDistance());
            boidCircles.add(circle);
            pane.getChildren().add(circle);
        }

        for(int i = 0; i < boids.size(); i++) {

            Polygon polygon = new Polygon();
            double x = boids.get(i).getX();
            double y = boids.get(i).getY();
            double x2 = boids.get(i).getX();
            double y2 = boids.get(i).getY();
            double x3 = boids.get(i).getX();
            double y3 = boids.get(i).getY();
            polygon.getPoints().addAll(x,y,x2,y2,x3,y3);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for(int i = 0; i < boids.size(); i++) {
                    boidCircles.get(i).setCenterX(boids.get(i).getX());
                    boidCircles.get(i).setCenterY(boids.get(i).getY());
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