import javafx.scene.control.Button;
import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;
    private Button btnSubmit;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(640, 480);

        btnSubmit = new Button("Submit");

        VBox mainBox = new VBox();
        HBox topBar = new HBox();
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));

        topBar.getChildren().add(btnSubmit);
        
        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("300"));
        topBar.getChildren().add(v4 = new TextField("10"));
                
        v1.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v2.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v3.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v4.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {

        graphics.translate((640/2),(480/2));
        graphics.scale(0.25,0.25);

        btnSubmit.setOnAction(event ->
        {
            float oldX1 = 0;
            float oldY1 = 0;

            for (float i = 0; i < Math.PI * Math.PI; i += 0.0001)
            {

                float x1 = (float)(Double.parseDouble(v1.getText()) * Math.cos(Double.parseDouble(v2.getText())*i)  + Double.parseDouble(v3.getText()) * Math.cos(Double.parseDouble(v4.getText()) * i));
                float y1 = (float)(Double.parseDouble(v1.getText()) * Math.sin(Double.parseDouble(v2.getText())*i)  + Double.parseDouble(v3.getText()) * Math.sin(Double.parseDouble(v4.getText()) * i));
                float x2;
                float y2;

                if (oldX1 == 0 && oldY1 == 0)
                {
                    x2 = x1;
                    y2 = y1;
                }
                else
                {
                    x2 = oldX1;
                    y2 = oldY1;
                }

                oldX1 = x1;
                oldY1 = y1;

                graphics.setColor(Color.getHSBColor(i / (float) Math.PI, 1, 1));
                graphics.draw(new Line2D.Double(x1, y1, x2, y2));
            }
        });

    }
    
    
    
    public static void main(String[] args) {
        launch(Spirograph.class);
    }

}
