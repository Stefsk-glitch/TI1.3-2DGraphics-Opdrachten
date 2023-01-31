import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage)
    {
        canvas = new Canvas(640, 480);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.translate(this.canvas.getWidth() / 2, this.canvas.getHeight() / 2);
        graphics.scale(0.5, -0.5);

        double res = 0.01;

        double x1;
        double x2;
        double y1;
        double y2;

        // math.pi = 3.14

        for (double i = 0; i < Math.PI; i += res)
        {
            // binnen
            y1 = 100 * Math.sin(i);
            x1 = 100 * Math.cos(i);
            System.out.println(x1);
            System.out.println(y1);

            // buiten
            x2 = 200 * Math.cos(i);
            y2 = 200 * Math.sin(i);
            System.out.println(x2);
            System.out.println(y2);

            graphics.setColor(Color.getHSBColor((float) (1 / Math.PI * i), 1, 1));
            graphics.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
