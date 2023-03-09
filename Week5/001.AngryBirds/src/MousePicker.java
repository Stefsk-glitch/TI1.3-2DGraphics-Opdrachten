
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.DetectResult;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.MotorJoint;
import org.dyn4j.geometry.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by johan on 2017-03-08.
 */
public class MousePicker {
    private Point2D mousePos = null;
    private Point2D lastPos = null;

    private Body body;
    private MotorJoint joint;
    private AffineTransform transform;
    private Body lastBody = null;
    private AngryBirds angryBirds;

    public MousePicker(Node node, AngryBirds angryBirds) throws IOException {
        this.angryBirds = angryBirds;

        EventHandler<? super MouseEvent> oldMousePressed = node.getOnMousePressed();
        EventHandler<? super MouseEvent> oldMouseReleased = node.getOnMouseReleased();
        EventHandler<? super MouseEvent> oldMouseDragged = node.getOnMouseDragged();

        node.setOnMousePressed(e -> {
            if (oldMousePressed != null)
                oldMousePressed.handle(e);
            this.mousePos = new Point2D.Double(e.getX(), e.getY());
            lastPos = mousePos;
        });

        node.setOnMouseReleased(e -> {
            if (oldMouseReleased != null)
                oldMouseReleased.handle(e);
            lastPos = mousePos;
            this.mousePos = null;
        });

        node.setOnMouseDragged(e -> {
            if (oldMouseDragged != null)
                oldMouseDragged.handle(e);
            this.mousePos = new Point2D.Double(e.getX(), e.getY());
            lastPos = mousePos;
        });

    }


    public void update(World world, AffineTransform transform, double scale) {
        this.transform = transform;
        if (mousePos == null) {
            if (body != null) {
                world.removeBody(body);
                world.removeJoint(joint);
                body = null;
                lastBody = null;
                joint = null;
            }
            return;
        }

        try {
            Point2D localMouse = transform.inverseTransform(mousePos, null);
            localMouse = new Point2D.Double(localMouse.getX() / scale, localMouse.getY() / -scale);

            if (body == null && joint == null) {

                Convex convex = Geometry.createCircle(0.1);
                Transform tx = new Transform();
                tx.translate(localMouse.getX(), localMouse.getY());

                // detect bodies under the mouse pointer
                List<DetectResult> results = new ArrayList<>();

                boolean detect = world.detect(
                        convex,
                        tx,
                        null,      // no, don't filter anything using the Filters
                        false,      // include sensor fixtures
                        false,      // include inactive bodies
                        false,      // we don't need collision info
                        results);

                if (detect) {
                    Body target = results.get(0).getBody();
                    if (!angryBirds.getBoxes().contains(target) && !angryBirds.isBirdThrown()) {
                        lastBody = target;
                        target.setAutoSleepingEnabled(false);
                        target.setAsleep(false);
                        body = new Body();
                        body.setMass(MassType.INFINITE);
                        body.addFixture(convex);
                        body.getTransform().setTranslation(localMouse.getX(), localMouse.getY());
                        world.addBody(body);

                        joint = new MotorJoint(target, body);
                        joint.setCollisionAllowed(false);
                        joint.setMaximumForce(1000.0);
                        joint.setMaximumTorque(0.01);

                        world.addJoint(joint);
                    }
                }
            }

            if (body != null) {
//                if (!angryBirds.getBoxes().contains(lastBody) && !angryBirds.isBallThrown()) {
                    body.getTransform().setTranslation(localMouse.getX(), localMouse.getY());
//                }
            }
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    public Point2D getMousePos() {
        Point2D localMouse = null;
        if (lastPos != null) {
            try {
                localMouse = transform.inverseTransform(lastPos, null);
            } catch (NoninvertibleTransformException e) {
                throw new RuntimeException(e);
            }
            return localMouse;
        } else return null;
    }

    public void setMousePos(Point2D mousePos) {
        this.lastPos = mousePos;
    }

    public Body getBody() {
        return lastBody;
    }

    public Body getBody2() {
        return body;
    }
}
