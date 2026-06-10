package bt.ahaus.bierpong1;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.Set;

public class AimSystem {

    private final Circle player;
    private final Set<KeyCode> keys;
    private final Line line;

    private double angle = 0;
    private boolean active = true;

    public AimSystem(AnchorPane pane, Circle player, Set<KeyCode> keys) {

        this.player = player;
        this.keys = keys;

        line = new Line();
        line.setStroke(Color.LIMEGREEN);
        line.setStrokeWidth(3);

        pane.getChildren().add(line);
    }

    public void update() {

        if (keys.contains(KeyCode.E))
            active = !active;

        if (!active) {
            line.setVisible(false);
            return;
        }

        line.setVisible(true);

        if (keys.contains(KeyCode.W)) angle -= 2;
        if (keys.contains(KeyCode.S)) angle += 2;

        double len = 120;

        double x1 = player.getCenterX();
        double y1 = player.getCenterY();

        double x2 = x1 + Math.cos(Math.toRadians(angle)) * len;
        double y2 = y1 + Math.sin(Math.toRadians(angle)) * len;

        line.setStartX(x1);
        line.setStartY(y1);

        line.setEndX(x2);
        line.setEndY(y2);
    }

    public double getAngle() {
        return angle;
    }

    public Circle getPlayer() {
        return player;
    }
}