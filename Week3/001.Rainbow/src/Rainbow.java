import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Rainbow extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Font font = new Font("Dialog", Font.BOLD, 48);

        graphics.setFont(font);

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.orange);
        colors.add(Color.yellow);
        colors.add(Color.green);
        colors.add(Color.cyan);
        colors.add(Color.lightGray);
        colors.add(Color.blue);
        colors.add(Color.MAGENTA);
        colors.add(Color.pink);
        colors.add(Color.orange);

        ArrayList<String> raindowString = new ArrayList<>();
        raindowString.add("r");
        raindowString.add("e");
        raindowString.add("g");
        raindowString.add("e");
        raindowString.add("n");
        raindowString.add("b");
        raindowString.add("o");
        raindowString.add("o");
        raindowString.add("g");

        int x = 110;
        int y = 300;
        int count = 0;
        float angle = 270;

        for (int i = 0; i < 9; i++) {
            AffineTransform tx = new AffineTransform();
            //tx.translate(400,400);
            tx.rotate(Math.toRadians(angle), x, y);
            //tx.scale(0.75f, 0.75f);
            graphics.setTransform(tx);
            graphics.setColor(colors.get(i));
            graphics.drawString(raindowString.get(i), x, y);
            x += 50;
            count++;
            angle += 22.5;

            if (count > 4)
            {
                y += 25;
            }
            else
            {
                y -= 25;
            }
        }
    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
