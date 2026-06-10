package bt.ahaus.bierpong1;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.Set;

public class GameManager {

    private final AnchorPane pane;
    private final Circle p1;
    private final Circle p2;
    private final Set<KeyCode> keys;

    private final AimSystem aimSystem;
    private final BallSystem ballSystem;

    public GameManager(AnchorPane pane, Circle p1, Circle p2, Set<KeyCode> keys) {

        this.pane = pane;
        this.p1 = p1;
        this.p2 = p2;
        this.keys = keys;

        this.aimSystem = new AimSystem(pane, p1, keys);
        this.ballSystem = new BallSystem(pane, p1, aimSystem);
    }

    public void start() {

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                aimSystem.update();
                ballSystem.update();
            }
        };

        timer.start();
    }
}