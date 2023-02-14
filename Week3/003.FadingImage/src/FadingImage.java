import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class FadingImage extends Application
{
    private ResizableCanvas canvas;
    private float floatValue = 0f;
    private File frogFile = new File("C:\\Users\\Stef\\Desktop\\Git repos\\TI1.3-2DGraphics-Opdrachten\\Week3\\003.FadingImage\\Images\\frog.png");
    private File parrotFile = new File("C:\\Users\\Stef\\Desktop\\Git repos\\TI1.3-2DGraphics-Opdrachten\\Week3\\003.FadingImage\\Images\\parrot.png");
    private Image targetImage;
    private boolean flipper = false;
    
    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> {
            try {
                draw(g);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer()
        {
            long last = -1;
            @Override
            public void handle(long now)
            {
                if(last == -1)
                {
                    last = now;
                }

                update((now - last) / 1000000000.0);
                last = now;
                try {
                    draw(g2d);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
        
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }
    
    
    public void draw(FXGraphics2D graphics) throws IOException
    {

        FileInputStream fis = null;

        if (flipper == false)
        {
            fis = new FileInputStream(parrotFile);
        }
        else
        {
            fis = new FileInputStream(frogFile);
        }

        targetImage = ImageIO.read(fis); //reading the image file

        AffineTransform tx = new AffineTransform();
        graphics.setTransform(tx);
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, floatValue));
        graphics.drawImage(targetImage, tx, null);
    }


    public void update(double deltaTime)
    {
        if (floatValue + 0.01f > 1f)
        {
            flipper = !flipper;
            floatValue = 0f;
        }
        if (floatValue + 0.01f < 1f)
        {
            floatValue += 0.01f;
        }
    }

    public static void main(String[] args)
    {
        launch(FadingImage.class);
    }
}
