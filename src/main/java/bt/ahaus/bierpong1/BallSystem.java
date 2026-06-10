package bt.ahaus.bierpong1;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.Set;

public class BallSystem {

    private final AnchorPane pane;
    private final Circle player;
    private final AimSystem aim;

    private Circle ball;

    private double vx;
    private double vy;
    private boolean flying = false;

    public BallSystem(AnchorPane pane, Circle player, AimSystem aim) {
        this.pane = pane;
        this.player = player;
        this.aim = aim;
    }

    public void update() {

        if (ball == null) return;

        if (flying) {

            ball.setCenterX(ball.getCenterX() + vx);
            ball.setCenterY(ball.getCenterY() + vy);

            vy += 0.2;
            vx *= 0.99;
        }
    }

    public void shoot(Set<KeyCode> keys) {

        if (flying) return;

        if (!keys.contains(KeyCode.SPACE)) return;

        ball = new Circle(6);
        ball.setCenterX(player.getCenterX());
        ball.setCenterY(player.getCenterY());

        pane.getChildren().add(ball);

        double angle = aim.getAngle();

        vx = Math.cos(Math.toRadians(angle)) * 10;
        vy = Math.sin(Math.toRadians(angle)) * 10;

        flying = true;
    }
}