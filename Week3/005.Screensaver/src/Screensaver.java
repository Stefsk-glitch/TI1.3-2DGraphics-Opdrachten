import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application
{
    private ResizableCanvas canvas;
    private ArrayList<Position> positions = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer()
        {
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

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        System.setProperty("myColor", "#8A008B");
        graphics.setColor(Color.getColor("myColor"));

        for (Position position : positions)
        {
            if (positions.size()+1 > 40)
            {
                for (int i = 0; i < 4; i++)
                {
                    positions.remove(i);
                    break;
                }
            }
            else
            {
                graphics.draw(new Line2D.Double(position.getX(), position.getY(), position.getX(), position.getY()));
            }
        }

        drawBetweenPositions(graphics);
    }

    private void drawBetweenPositions(FXGraphics2D graphics)
    {
        for (int i = 0; i < positions.size(); i = i + 4)
        {
            Position pos1 = positions.get(i);
            Position pos2 = positions.get(i+1);
            Position pos3 = positions.get(i+2);
            Position pos4 = positions.get(i+3);

            graphics.draw(new Line2D.Double(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY()));
            graphics.draw(new Line2D.Double(pos2.getX(), pos2.getY(), pos3.getX(), pos3.getY()));
            graphics.draw(new Line2D.Double(pos3.getX(), pos3.getY(), pos4.getX(), pos4.getY()));
            graphics.draw(new Line2D.Double(pos4.getX(), pos4.getY(), pos1.getX(), pos1.getY()));
        }
    }

    public void init()
    {
        Random r = new Random();

        Position startPosition1 = new Position(55, 55, r.nextInt(4) + 1);
        Position startPosition2 = new Position(55, 300, r.nextInt(4) + 1);
        Position startPosition3 = new Position(300, 300, r.nextInt(4) + 1);
        Position startPosition4 = new Position(300, 55, r.nextInt(4) + 1);

        positions.add(startPosition1);
        positions.add(startPosition2);
        positions.add(startPosition3);
        positions.add(startPosition4);
    }

    public void update(double deltaTime)
    {
//        System.out.println(canvas.getHeight());
//        System.out.println(canvas.getWidth());

        for (Position position : positions)
        {
            if (position.getDirection() == 1)
            {
                if (position.getX() - 1 > 0 && position.getY() - 1 > 0)
                {
                    position.setX(position.getX() - 1);
                    position.setY(position.getY() - 1);
                }
                else
                {
                    Random r = new Random();
                    int value = position.getDirection();

                    while (position.getDirection() == value)
                    {
                        value = r.nextInt(4) + 1;
                    }

                    position.setDirection(value);
                }
            }

            if (position.getDirection() == 2)
            {
                if (position.getX() + 1 < canvas.getWidth() && position.getY() - 1 > 0)
                {

                    position.setX(position.getX() + 1);
                    position.setY(position.getY() - 1);
                }
                else
                {
                    Random r = new Random();
                    int value = position.getDirection();

                    while (position.getDirection() == value)
                    {
                        value = r.nextInt(4) + 1;
                    }

                    position.setDirection(value);
                }
            }

            if (position.getDirection() == 3)
            {
                if (position.getX() + 1 < canvas.getWidth() && position.getY() + 1 < canvas.getHeight())
                {

                    position.setX(position.getX() + 1);
                    position.setY(position.getY() + 1);
                }
                else
                {
                    Random r = new Random();
                    int value = position.getDirection();

                    while (position.getDirection() == value)
                    {
                        value = r.nextInt(4) + 1;
                    }

                    position.setDirection(value);
                }
            }

            if (position.getDirection() == 4)
            {
                if (position.getX() + 1 < canvas.getWidth() && position.getY() - 1 > 0)
                {

                    position.setX(position.getX() + 1);
                    position.setY(position.getY() - 1);
                }
                else
                {
                    Random r = new Random();
                    int value = position.getDirection();

                    while (position.getDirection() == value)
                    {
                        value = r.nextInt(4) + 1;
                    }

                    position.setDirection(value);
                }
            }
        }
    }

    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }
}
