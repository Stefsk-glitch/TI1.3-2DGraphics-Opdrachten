import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application
{
    private ResizableCanvas canvas;

    private ArrayList<Block> blocks = new ArrayList<>();

    private Random random = new Random();

    ArrayList<Color> colors = new ArrayList<>();

    private double x;
    private double y;

    private Block selectedBlock;


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.DARK_GRAY);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.MAGENTA);
        colors.add(Color.ORANGE);
        colors.add(Color.PINK);
        colors.add(Color.RED);
        colors.add(Color.WHITE);
        colors.add(Color.YELLOW);

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);

        canvas.resize(1920,1080);

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        for (int i = 0; i < 12; i++)
        {
            blocks.add(new Block(100,100,100,100, colors.get(i)));
        }

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.draw((new Rectangle2D.Double()));
        Color c = graphics.getColor();

        for (Block block : blocks)
        {
            graphics.setColor(block.getColor());
            graphics.fill(block);
            graphics.setColor(Color.BLACK);
            graphics.draw(block);
        }
    }

    public static void main(String[] args)
    {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent event)
    {
        for (Block block : blocks)
        {
            if (block.contains(event.getX(), event.getY()))
            {
                selectedBlock = block;
                x = selectedBlock.getX() - event.getX();
                y = selectedBlock.getY() - event.getY();
            }
        }
    }

    private void mouseReleased(MouseEvent event)
    {
        selectedBlock = null;
    }

    private void mouseDragged(MouseEvent event)
    {
        if (selectedBlock != null)
        {
            selectedBlock.setPos(event.getX() + x, event.getY() + y);
        }
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }
}
