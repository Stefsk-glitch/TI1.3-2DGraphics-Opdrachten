import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class House extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Canvas canvas = new Canvas(640, 480);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics)
    {
        // house
        graphics.drawLine(400,400,100,400);
        graphics.drawLine(400,400,400,200);
        graphics.drawLine(100, 400, 100, 200);
        graphics.drawLine(100, 200, 250, 50);
        graphics.drawLine(400, 200, 250, 50);

        // door
        graphics.drawLine(150, 400, 150, 300);
        graphics.drawLine(150, 300, 200, 300);
        graphics.drawLine(200, 300, 200, 400);

        // window
        graphics.drawLine(250, 275, 375,275);
        graphics.drawLine(250, 350, 375,350);
        graphics.drawLine(375, 350, 375,275);
        graphics.drawLine(250, 275, 250,350);

    }



    public static void main(String[] args)
    {
        launch(House.class);
    }

}
