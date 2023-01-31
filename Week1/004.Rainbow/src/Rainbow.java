import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Canvas canvas = new Canvas(1280, 720);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics)
    {
        for(int i = 0; i < 500; i++) {
            graphics.setColor(Color.getHSBColor(i/500.0f, 1, 1));
            graphics.drawLine(10+i, 719-i, 100+i, 719-i);
        }
    }
    
    
    
    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
