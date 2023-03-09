import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class RopeConstraint implements Constraint, Serializable
{
    private double distance;
    private Particle a;
    private Particle b;
    private double forceConstraint;

    private Color color;
    private float greenValueColor = (1f / 360f) * 120f;

    public RopeConstraint(Particle a, Particle b)
    {
        this(a, b, a.getPosition().distance(b.getPosition()));
    }

    public RopeConstraint(Particle a, Particle b, double distance)
    {
        this.a = a;
        this.b = b;
        this.distance = distance;
        this.forceConstraint = 0.0;
        this.color = Color.red;
    }

    @Override
    public void satisfy()
    {
        double currentDistance = a.getPosition().distance(b.getPosition());
        if (currentDistance > distance)
        {
            this.forceConstraint = (currentDistance - distance) / 2;

            float hue = greenValueColor;

            if (forceConstraint > 0)
            {
                hue = (float) (1 / 360f * (120 - (10 * forceConstraint)));
            }

            if (hue > greenValueColor)
            {
                hue = greenValueColor;
            }

            if (hue < 0.0f)
            {
                hue = 0.0f;
            }

            this.color = Color.getHSBColor(hue, 1, 1);

            Point2D BA = new Point2D.Double(b.getPosition().getX() - a.getPosition().getX(), b.getPosition().getY() - a.getPosition().getY());
            double length = BA.distance(0, 0);
            if (length > 0.0001) // We kunnen alleen corrigeren als we een richting hebben
            {
                BA = new Point2D.Double(BA.getX() / length, BA.getY() / length);
            }
            else
            {
                BA = new Point2D.Double(1, 0);
            }

            a.setPosition(new Point2D.Double(a.getPosition().getX() + BA.getX() * forceConstraint,
                    a.getPosition().getY() + BA.getY() * forceConstraint));
            b.setPosition(new Point2D.Double(b.getPosition().getX() - BA.getX() * forceConstraint,
                    b.getPosition().getY() - BA.getY() * forceConstraint));
        }
    }

    @Override
    public void draw(FXGraphics2D g2d)
    {
        g2d.setColor(getColor());
        g2d.draw(new Line2D.Double(a.getPosition(), b.getPosition()));
    }

    public double getForceConstraint()
    {
        return this.forceConstraint;
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
