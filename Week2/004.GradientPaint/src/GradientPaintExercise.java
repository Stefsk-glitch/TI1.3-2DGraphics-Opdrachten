import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private Point2D center;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        //canvas.resize(640, 480);

        center = new Point2D.Double((int)canvas.getWidth() / 2, (int)canvas.getHeight() / 2);

        FXGraphics2D graphics = new FXGraphics2D(canvas.getGraphicsContext2D());

        canvas.setOnMouseDragged( event -> {
            center = new Point2D.Double(event.getX(), event.getY());
            draw(graphics);
        });

        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(graphics);
    }


    public void draw(FXGraphics2D graphics)
    {
//        GradientPaint gp1 = new GradientPaint(0, 0, Color.green, 640, 480, Color.black);
//        graphics.setPaint(gp1);
//        graphics.fillRect(00, 00, 640, 480);

        float radius = 60;
        float[] dist = {0.0f, 0.2f, 1.0f};
        Color[] colors = {Color.RED, Color.WHITE, Color.BLUE};
        RadialGradientPaint radialGradientPaint = new RadialGradientPaint(center, radius, dist, colors);
        graphics.setPaint(radialGradientPaint);
        graphics.fillRect(00, 00, 640, 480);
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
