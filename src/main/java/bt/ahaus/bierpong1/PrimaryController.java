package bt.ahaus.bierpong1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

public class PrimaryController {

    @FXML private AnchorPane gamePane;

    @FXML private Circle player1;
    @FXML private Circle player2;

    private final double speed = 5;
    private final Set<KeyCode> keys = new HashSet<>();

    @FXML
    public void initialize() {

        player1.sceneProperty().addListener((obs, oldScene, newScene) -> {

            if (newScene != null) {

                newScene.setOnKeyPressed(e -> keys.add(e.getCode()));
                newScene.setOnKeyReleased(e -> keys.remove(e.getCode()));

                startLoop();
            }
        });
    }

    private void startLoop() {

        Thread t = new Thread(() -> {

            while (true) {

                Platform.runLater(() -> {
                    movePlayers();
                    handleCollision();
                    wallCollision(player1);
                    wallCollision(player2);
                });

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

    private void movePlayers() {

        if (keys.contains(KeyCode.W)) player1.setCenterY(player1.getCenterY() - speed);
        if (keys.contains(KeyCode.S)) player1.setCenterY(player1.getCenterY() + speed);
        if (keys.contains(KeyCode.A)) player1.setCenterX(player1.getCenterX() - speed);
        if (keys.contains(KeyCode.D)) player1.setCenterX(player1.getCenterX() + speed);

        if (keys.contains(KeyCode.UP)) player2.setCenterY(player2.getCenterY() - speed);
        if (keys.contains(KeyCode.DOWN)) player2.setCenterY(player2.getCenterY() + speed);
        if (keys.contains(KeyCode.LEFT)) player2.setCenterX(player2.getCenterX() - speed);
        if (keys.contains(KeyCode.RIGHT)) player2.setCenterX(player2.getCenterX() + speed);
    }

    private void handleCollision() {

        double dx = player1.getCenterX() - player2.getCenterX();
        double dy = player1.getCenterY() - player2.getCenterY();

        double dist = Math.sqrt(dx * dx + dy * dy);

        double min = player1.getRadius() + player2.getRadius();

        if (dist < min && dist != 0) {

            double nx = dx / dist;
            double ny = dy / dist;

            double push = (min - dist) / 2;

            player1.setCenterX(player1.getCenterX() + nx * push);
            player1.setCenterY(player1.getCenterY() + ny * push);

            player2.setCenterX(player2.getCenterX() - nx * push);
            player2.setCenterY(player2.getCenterY() - ny * push);
        }
    }

    private void wallCollision(Circle p) {

        double r = p.getRadius();

        double w = gamePane.getWidth();
        double h = gamePane.getHeight();

        if (w <= 0 || h <= 0) return;

        if (p.getCenterX() < r) p.setCenterX(r);
        if (p.getCenterX() > w - r) p.setCenterX(w - r);

        if (p.getCenterY() < r) p.setCenterY(r);
        if (p.getCenterY() > h - r) p.setCenterY(h - r);
    }
}