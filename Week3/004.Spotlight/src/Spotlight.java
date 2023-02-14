import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application
{
    private ResizableCanvas canvas;
    private int x = 200;
    private int y = 200;
    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        canvas.setOnMouseDragged(event -> {
            x = (int)(event.getX());
            y = (int)(event.getY());
            draw(g2d);
        });

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Shape shape = new Ellipse2D.Double(x - 100, y - 100, 200, 200);
        graphics.setClip(shape);
        graphics.draw(shape);



        Random r = new Random();
        for(int i = 0; i < 1000; i++)
        {
            graphics.setPaint(Color.getHSBColor(r.nextFloat(),1,1));
            graphics.drawLine((int)(r.nextInt() % canvas.getWidth()), (int)(r.nextInt() % canvas.getHeight()), (int)(r.nextInt() % canvas.getWidth()), (int)(r.nextInt() % canvas.getHeight()));
        }
        graphics.setClip(null);
    }

    public void init()
    {

    }

    public void update(double deltaTime)
    {

    }

    public static void main(String[] args)
    {
        launch(Spotlight.class);
    }

}
