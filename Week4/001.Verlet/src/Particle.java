import org.jfree.fx.FXGraphics2D;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Particle implements Serializable
{

    private Point2D position;
    private Point2D lastPosition;
    private double size;

    public Particle(Point2D position) {
        this.position = position;
        this.lastPosition = new Point2D.Double(position.getX(), position.getY());
        this.size = 20;
    }

    public void update(int width, int height) {
        Point2D previousValue = position;
        Point2D velocity = new Point2D.Double(
                position.getX() - lastPosition.getX(),
                position.getY() - lastPosition.getY());

        position = new Point2D.Double(position.getX() + velocity.getX(),
                position.getY() + velocity.getY() + 0.9);

        if (position.getX() < 0) {
            position = new Point2D.Double(0, position.getY());
        }
        if (position.getX() > width) {
            position = new Point2D.Double(width, position.getY());
        }
        if (position.getY() < 0) {
            position = new Point2D.Double(position.getX(), 0);
        }
        if (position.getY() > height) {
            position = new Point2D.Double(position.getX(), height);
        }

        lastPosition = previousValue;
    }

    public void draw(FXGraphics2D g2d) {
        Shape shape = new Ellipse2D.Double(position.getX() - size / 2, position.getY() - size / 2, size, size);
        g2d.setColor(Color.pink);
        g2d.fill(shape);
        g2d.setColor(Color.black);
        g2d.draw(shape);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}
