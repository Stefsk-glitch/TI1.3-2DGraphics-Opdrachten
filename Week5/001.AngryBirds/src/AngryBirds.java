
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import static javax.swing.JOptionPane.showMessageDialog;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class AngryBirds extends Application
{
    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Body canon;
    private PinJoint canonJoint;
    private Body bird;
    private boolean canonReleased;
    private boolean birdThrown;
    private boolean wonGame = false;
    private ArrayList<Body> boxes = new ArrayList<>();
    private ArrayList<GameObject> boxesAsGameObjects = new ArrayList<>();

    public AngryBirds() throws IOException
    {
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });

        mainPane.setTop(showDebug);
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        canvas.setOnMouseReleased(event -> {
            if (mousePicker.getBody() != null)
            {
                if (mousePicker.getBody().equals(bird))
                {
                    throwMonkey();
                }
            }
        });

        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas, this);


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

        stage.setScene(new Scene(mainPane, 1280, 720));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void init()
    {
        debugSelected = false;
        canonReleased = false;
        birdThrown = false;

        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(100, 1));
        floor.getFixture(0).setFriction(3);
        floor.getTransform().setTranslation(1, -5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        gameObjects.add(new GameObject("floor.png", floor, new Vector2(0, -175), 1));

        Body leftWall = new Body();
        leftWall.addFixture(Geometry.createRectangle(1, 1000));
        leftWall.getFixture(0).setFriction(0.6);
        leftWall.getTransform().setTranslation(-10.2, 0);
        leftWall.setMass(MassType.INFINITE);
        world.addBody(leftWall);

        Body rightWall = new Body();
        rightWall.addFixture(Geometry.createRectangle(1, 1000));
        rightWall.getFixture(0).setFriction(0.6);
        rightWall.getTransform().setTranslation(10.2, 0);
        rightWall.setMass(MassType.INFINITE);
        world.addBody(rightWall);

        this.canon = new Body();
        this.canon.getTransform().setTranslation(-7.25, -2.85);
        this.canon.setMass(MassType.INFINITE);
        world.addBody(this.canon);

        Body catapult = new Body();
        catapult.getTransform().setTranslation(-7.2, -3.5);
        catapult.setMass(MassType.INFINITE);
        world.addBody(catapult);
        gameObjects.add(new GameObject("canon.png", catapult, new Vector2(225, 0), 0.25));

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3 - y; x++)
            {
                Body box = new Body();
                box.addFixture(Geometry.createRectangle(0.5, 0.5));
                box.setMass(MassType.NORMAL);
                box.getTransform().setTranslation(4 + (x * 0.5) + 0.25 * y, (y * 0.5) - 4.25);
                world.addBody(box);
                GameObject gameObject = new GameObject("box.png", box, new Vector2(0, 0), 0.2);
                gameObjects.add(gameObject);
                boxes.add(box);
                boxesAsGameObjects.add(gameObject);
            }
        }

        bird = new Body();
        bird.addFixture(Geometry.createPolygonalCircle(20, 0.15));
        bird.getTransform().setTranslation(this.canon.getTransform().getTranslation());
        bird.getContacts(false);
        Mass mass = new Mass(new Vector2(0, 0), 5, 0.000795);
        bird.setMass(mass);
        bird.getFixture(0).setRestitution(0.2);
        bird.getFixture(0).setFriction(0.3);
        world.addBody(bird);
        gameObjects.add(new GameObject("angryBird.png", bird, new Vector2(0, -50), 0.06));

        canonJoint = new PinJoint(bird, this.canon.getTransform().getTranslation(), 10, 0, 500);
        canonJoint.setTarget(this.canon.getTransform().getTranslation());
        world.addJoint(canonJoint);
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.getHSBColor(360 / 80f, 0.3f, 0.8f));
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);
        graphics.setColor(Color.getHSBColor(360 / 340f, 0.57f, 0.8f));

        if (!birdThrown)
        {
            if (mousePicker.getBody() != null && mousePicker.getMousePos() != null)
            {
                if (mousePicker.getBody().equals(bird))
                {
                    graphics.drawLine((int) mousePicker.getMousePos().getX(), (int) -mousePicker.getMousePos().getY(), -700, -280);
                    graphics.drawLine((int) mousePicker.getMousePos().getX(), (int) -mousePicker.getMousePos().getY(), -740, -285);
                }
            }

        }

        for (GameObject go : gameObjects)
        {
            go.draw(graphics);
        }

        if (debugSelected)
        {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime)
    {
        if (wonGame)
        {
            wonGame = false;
            showMessageDialog(null, "You win!");
        }
        if (birdThrown)
        {
            if (bird.getChangeInPosition().x < 0.0005 && bird.getChangeInPosition().x > -0.0005 && bird.getChangeInPosition().y < 0.0005 && bird.getChangeInPosition().x > -0.0005 && bird.getForce().equals(new Vector2(0, 0)))
            {
                bird.getTransform().setTranslation(canon.getTransform().getTranslation());
                world.addJoint(canonJoint);
                birdThrown = false;
            }
        }

        if (canonReleased)
        {
            Vector2 ballToCatapult = new Vector2(-728, 282).subtract(new Vector2(mousePicker.getMousePos().getX(), mousePicker.getMousePos().getY()));
            ballToCatapult.set(ballToCatapult.x, ballToCatapult.y * -1);
            double forceMagnitude = 25.0;
            Vector2 force = ballToCatapult.product(forceMagnitude);
            System.out.println(force);

            if (force.x > 7000)
            {
                force.set(7000, force.y);
            }
            else if (force.x < -7000)
            {
                force.set(-7000, force.y);
            }

            if (force.y > 7000)
            {
                force.set(force.x, 7000);
            }
            else if (force.y < -7000)
            {
                force.set(force.x, -7000);
            }

            bird.applyForce(force);
            bird.applyImpulse(-0.002);
            birdThrown = true;
            mousePicker.setMousePos(null);
            canonReleased = false;
        }

        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);

        if (boxes.isEmpty())
        {
            gameObjects.clear();
            boxes.clear();
            boxesAsGameObjects.clear();
            world.removeAllBodiesAndJoints();
            wonGame = true;
            init();
        }
        else
        {
            for (Body box : boxes)
            {
                if (box.getAngularVelocity() > 4 || box.getAngularVelocity() < -4)
                {
                    gameObjects.remove(boxesAsGameObjects.get(boxes.indexOf(box)));
                    boxesAsGameObjects.remove(boxesAsGameObjects.get(boxes.indexOf(box)));
                    world.removeBody(box);
                    boxes.remove(box);
                    break;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        launch(AngryBirds.class);
    }

    public void throwMonkey()
    {
        canonReleased = true;
        world.removeJoint(canonJoint);
    }

    public Body getBird()
    {
        return bird;
    }

    public boolean isBirdThrown()
    {
        return birdThrown;
    }

    public ArrayList<Body> getBoxes()
    {
        return boxes;
    }
}