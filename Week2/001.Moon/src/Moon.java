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

public class Moon extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Moon");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(this.canvas.getWidth() / 2, this.canvas.getHeight() / 2);
        graphics.scale(4, -4);

//        Area moon = new Area(new Ellipse2D.Double(-100, -100, 200, 200));
//        Area cutout = new Area(new Ellipse2D.Double(-100 - 100, -100, 200, 200));
//        Area result = new Area(moon);
//        result.subtract(cutout);
//
//        graphics.fill(result);

        GeneralPath myShape = new GeneralPath();

        // first line
        myShape.moveTo(0, 10);
        myShape.quadTo(5, 15, 0, 20);

        // second line
        myShape.moveTo(0, 10);
        myShape.quadTo(12, 15, 0, 20);



        graphics.draw(myShape);
    }


    public static void main(String[] args)
    {
        launch(Moon.class);
    }

}
