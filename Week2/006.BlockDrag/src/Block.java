import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Block extends Rectangle2D.Double
{
    private double width;
    private double height;
    private double x;
    private double y;
    private Color color;

    public Block(double width, double height, double x, double y, Color color)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        setRect(x, y, width, height);
    }

    public void setPos(double x, double y)
    {
        this.x = x;
        this.y = y;
        setRect(x, y, width, height);
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }
}
