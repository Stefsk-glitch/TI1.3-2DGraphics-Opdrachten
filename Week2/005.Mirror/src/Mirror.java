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

public class Mirror extends Application {
    ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(300, 300);

        GeneralPath myShape = new GeneralPath();

        myShape.moveTo(0, 0);
        myShape.lineTo(100, 0);
        myShape.moveTo(100, 0);
        myShape.lineTo(100, 100);
        myShape.moveTo(100, 100);
        myShape.lineTo(0, 100);

        graphics.draw(myShape);

        graphics.setColor(Color.blue);

        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        graphics.draw(tx.createTransformedShape(myShape));
    }


    public static void main(String[] args)
    {
        launch(Mirror.class);
    }

}
