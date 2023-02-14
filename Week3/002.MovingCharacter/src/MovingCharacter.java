import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.MouseEvent;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private int x = 0;
    private int y = 0;
    private AffineTransform spriteChar;
    private BufferedImage image;
    private BufferedImage[] subImages;
    private boolean isFalling = false;
    private boolean isGoingBackwards = false;
    private int spriteIndex = 42;
    private int startSpriteIndex = 42;
    private int endSpriteIndex = 50;
    private double timer = 0;

    @Override
    public void start(Stage stage) throws Exception
    {
        try
        {
            File file = new File("C:\\Users\\Stef\\Desktop\\Git repos\\TI1.3-2DGraphics-Opdrachten\\Week3\\002.MovingCharacter\\resources\\images\\sprite.png");
            FileInputStream fis = new FileInputStream(file);
            image = ImageIO.read(fis); //reading the image file

            //cut the image in 65 pieces of 64x64 pixels.
            subImages = new BufferedImage[65];
            for (int i = 0; i < 65; i++)
            {
                subImages[i] = image.getSubimage(64 * (i % 8), 64 * (i / 8), 64, 64);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D graphics = new FXGraphics2D(canvas.getGraphicsContext2D());

        new AnimationTimer()
        {
            long lastTime = -1;

            @Override
            public void handle(long timeNow)
            {
                if (lastTime == -1)
                {
                    lastTime = timeNow;
                }

                update((timeNow - lastTime) / 1000000000.0);
                lastTime = timeNow;
                draw(graphics);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(graphics);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.darkGray);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        spriteChar = new AffineTransform();
        spriteChar.translate(x, y);

        canvas.setOnMousePressed(event -> mousePressed(event));

        if (isGoingBackwards)
        {
            spriteChar.scale(-1, 1);
        }

        graphics.drawImage(subImages[spriteIndex], spriteChar, null);
    }


    public void update(double time)
    {
        timer += time;

        if (spriteIndex == endSpriteIndex)
        {
            spriteIndex = startSpriteIndex;
        }

        if (x > canvas.getWidth() - subImages[spriteIndex].getWidth())
        {
            isGoingBackwards = true;
        }

        if (x < subImages[spriteIndex].getWidth())
        {
            isGoingBackwards = false;
        }

        if (!isGoingBackwards)
        {
            x += 4;
        }
        else
        {
            x -= 4;
        }

        if (timer > 0.06)
        {
            spriteIndex++;
            timer = 0;
        }

        if (isFalling)
        {
            if (spriteIndex == 25)
            {
                isFalling = false;
                spriteIndex = 42;
            }
            if (!isGoingBackwards)
            {
                x -= 4;
            }
            else
            {
                x += 4;
            }
        }
    }

    private void mousePressed(MouseEvent event)
    {
        isFalling = true;
        spriteIndex = 17;
    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }

}
