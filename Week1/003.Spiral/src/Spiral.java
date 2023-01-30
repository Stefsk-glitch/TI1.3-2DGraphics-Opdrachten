import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        canvas = new Canvas(400, 400);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics)
    {
        graphics.setColor(Color.red);
        graphics.drawLine(0,(int)this.canvas.getHeight()/2,1000,(int)this.canvas.getHeight()/2);

        graphics.setColor(Color.green);
        graphics.drawLine((int)this.canvas.getWidth()/2,0,(int)this.canvas.getWidth()/2,1000);

        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);

        graphics.setColor(Color.black);

        int nPoints = 720;

        int x1 = 0;
        int y1 = 0;
        int x2;
        int y2;

        for (int i = 0; i < nPoints; i++)
        {
            double t = i * Math.PI / 180;
            x2 = (int) ((int) 20*t*Math.cos(t));
            y2 = (int) ((int) 20*t*Math.sin(t));
            graphics.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }
    
    
    
    public static void main(String[] args)
    {
        launch(Spiral.class);
    }

}
