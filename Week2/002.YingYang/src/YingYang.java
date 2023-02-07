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

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(this.canvas.getWidth() / 2, this.canvas.getHeight() / 2);

        Area yinyangCircle = new Area(new Ellipse2D.Double(-100, -100, 200, 200));

        Area yangCircle = new Area(new Ellipse2D.Double(-50, 0, 100, 100));
        Area yangSmallCircle = new Area(new Ellipse2D.Double(-12.5, -50 - 12.5, 25, 25));

        Area yinCircle = new Area(new Ellipse2D.Double(-50, -100, 100, 100));
        Area yinSmallCircle = new Area(new Ellipse2D.Double(-12.5, 25 + 12.5, 25, 25));

        yangCircle.add(yangSmallCircle);
        yangCircle.subtract(yinSmallCircle);
        yinCircle.add(yinSmallCircle);
        yinCircle.subtract(yangSmallCircle);

        Area yangSideRectangle = new Area(new Rectangle2D.Double(-100, -100, 100, 200));
        Area yinSideRectangle = new Area(new Rectangle2D.Double(0, -100, 100, 200));

        Area yinSide = new Area(yinyangCircle);
        yinSide.subtract(yangSideRectangle);
        yinSide.subtract(yangCircle);
        yinSide.add(yinCircle);

        Area yangSide = new Area(yinyangCircle);
        yangSide.subtract(yinSideRectangle);
        yangSide.subtract(yinCircle);
        yangSide.add(yangCircle);

        graphics.setColor(Color.WHITE);
        graphics.fill(yangSide);
        graphics.setColor(Color.BLACK);
        graphics.fill(yinSide);
        graphics.draw(yinyangCircle);
    }


    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
