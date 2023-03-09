
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class VerletEngine extends Application
{

    private ResizableCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);

    private Button btnCover = new Button("Cover");
    private Button btnSave = new Button("Save");
    private Button btnLoad = new Button("Load");
    private HBox hBox = new HBox(10);

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer()
        {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        // Mouse Events
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        btnCover.setOnAction(e -> cover());
        btnSave.setOnAction(e -> save("filename.txt"));
        btnLoad.setOnAction(e -> load("filename.txt"));

        hBox.getChildren().addAll(btnLoad, btnSave, btnCover);

        mainPane.setTop(hBox);


        stage.setScene(new Scene(mainPane));
        stage.setTitle("Verlet Engine");
        stage.show();
        draw(g2d);
    }

    public void init()
    {
        for (int i = 0; i < 20; i++)
        {
            particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        }

        for (int i = 0; i < 10; i++)
        {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));
        }

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint c : constraints)
        {
            c.draw(graphics);
        }

        for (Particle p : particles)
        {
            p.draw(graphics);
        }
    }

    private void update(double deltaTime)
    {
        for (Particle p : particles)
        {
            p.update((int) canvas.getWidth(), (int) canvas.getHeight());
        }

        for (Constraint c : constraints)
        {
            c.satisfy();
        }
    }

    private void mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseButton.PRIMARY)
        {

            Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
            Particle newParticle = new Particle(mousePosition);
            Particle nearest = getNearest(mousePosition);

            if (e.isShiftDown())
            {
                particles.add(newParticle);
                constraints.add(new RopeConstraint(newParticle, nearest, 150));
            }
            else
            {
                particles.add(newParticle);
                constraints.add(new DistanceConstraint(newParticle, nearest));
            }
        }
        else if (e.getButton() == MouseButton.SECONDARY)
        {
            if (e.isShiftDown())
            {
                Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
                Particle nearest1 = getNearest(mousePosition);
                Particle nearest2 = null;
                double distance1 = Double.MAX_VALUE, distance2 = Double.MAX_VALUE;
                for (Particle p : particles)
                {
                    double distance = p.getPosition().distance(mousePosition);
                    if (p != nearest1 && distance < distance2)
                    {
                        if (distance < distance1)
                        {
                            distance2 = distance1;
                            nearest2 = nearest1;
                            distance1 = distance;
                            nearest1 = p;
                        }
                        else
                        {
                            distance2 = distance;
                            nearest2 = p;
                        }
                    }
                }
                if (nearest1 != null && nearest2 != null)
                {
                    constraints.add(new DistanceConstraint(nearest1, nearest2));
                }
            }
            else
            {
                Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
                Particle nearest = getNearest(mousePosition);
                Particle newParticle = new Particle(mousePosition);
                particles.add(newParticle);
                constraints.add(new DistanceConstraint(newParticle, nearest));

                ArrayList<Particle> sorted = new ArrayList<>();
                sorted.addAll(particles);

                //sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
                Collections.sort(sorted, new Comparator<Particle>()
                {
                    @Override
                    public int compare(Particle o1, Particle o2)
                    {
                        return (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition));
                    }
                });

                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
            }
        }
        else if (e.getButton() == MouseButton.MIDDLE)
        {
            // Reset
            particles.clear();
            constraints.clear();
            init();
        }
        else if (e.isControlDown() && e.getButton() == MouseButton.SECONDARY)
        {
            Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
            Particle newParticle = new Particle(mousePosition);
            particles.add(newParticle);
            Particle nearest = getNearest(mousePosition);

            DistanceConstraint constraint1 = new DistanceConstraint(newParticle, nearest, 100);
            DistanceConstraint constraint2 = new DistanceConstraint(newParticle, particles.get(particles.indexOf(nearest) + 1), 100);

            constraints.add(constraint1);
            constraints.add(constraint2);
        }
        if (e.getButton() == MouseButton.PRIMARY && e.isControlDown())
        {
            Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
            Particle newParticle = new Particle(mousePosition);

            constraints.add(new StaticConstraint(newParticle));
        }
    }

    private Particle getNearest(Point2D point)
    {
        Particle nearest = particles.get(0);
        for (Particle p : particles)
        {
            if (p.getPosition().distance(point) < nearest.getPosition().distance(point))
            {
                nearest = p;
            }
        }
        return nearest;
    }

    public void save(String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            //write the particles and constraints to the file
            objectOutputStream.writeObject(particles);
            objectOutputStream.writeObject(constraints);

            objectOutputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            //read the particles and constraints from the file
            particles = (ArrayList<Particle>) objectInputStream.readObject();
            constraints = (ArrayList<Constraint>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void mousePressed(MouseEvent e)
    {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10)
        {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e)
    {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    private void cover()
    {
        particles.clear();
        constraints.clear();

        int width = 10;
        int height = 5;

        ArrayList<Particle> topRow = new ArrayList<>();
        for (int i = 0; i < width; i++)
        {
            Particle particle = new Particle(new Point2D.Double(50 + (50 * i), 50));
            StaticConstraint staticConstraint = new StaticConstraint(particle);

            if (topRow.size() > 0)
            {
                DistanceConstraint distanceConstraint = new DistanceConstraint(particle, topRow.get(i - 1));
                constraints.add(distanceConstraint);
            }
            topRow.add(particle);
            constraints.add(staticConstraint);
        }
        particles.addAll(topRow);

        ArrayList<Particle> oldParticleRow;
        oldParticleRow = topRow;
        ArrayList<Particle> currentParticleRow = new ArrayList<>();

        for (int i = 0; i < height - 1; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Particle particle = new Particle(new Point2D.Double(100 + (100 * j), (200 * i)));

                if (currentParticleRow.size() > 0)
                {
                    DistanceConstraint distanceConstraintHor = new DistanceConstraint(particle, currentParticleRow.get(j - 1));
                    constraints.add(distanceConstraintHor);
                }

                DistanceConstraint distanceConstraintVer = new DistanceConstraint(particle, oldParticleRow.get(j), 100);
                constraints.add(distanceConstraintVer);
                currentParticleRow.add(particle);
            }
            constraints.add(mouseConstraint);
            particles.addAll(currentParticleRow);
            oldParticleRow.clear();
            oldParticleRow.addAll(currentParticleRow);
            currentParticleRow.clear();
        }
    }

    public static void main(String[] args)
    {
        launch(VerletEngine.class);
    }

}
