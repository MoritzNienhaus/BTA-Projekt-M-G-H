package bt.ahaus.bierpong1;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

public class PrimaryController {

    @FXML
    private AnchorPane gamePane;

    @FXML
    private Circle player1;

    @FXML
    private Circle player2;

    private final double speed = 5;

    private final Set<KeyCode> pressedKeys = new HashSet<>();

    @FXML
    public void initialize() {

        player1.sceneProperty().addListener((obs, oldScene, newScene) -> {

            if (newScene != null) {

                Scene scene = newScene;

                scene.setOnKeyPressed(event ->
                        pressedKeys.add(event.getCode())
                );

                scene.setOnKeyReleased(event ->
                        pressedKeys.remove(event.getCode())
                );

                startGameLoop();
            }
        });
    }

    private void startGameLoop() {

        Thread gameThread = new Thread(() -> {

            try {

                while (true) {

                    javafx.application.Platform.runLater(() -> {

                        movePlayers();
                        handleCollision();
                        wallCollision(player1);
                        wallCollision(player2);

                    });

                    Thread.sleep(16);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void movePlayers() {

        // Spieler 1 (WASD)

        if (pressedKeys.contains(KeyCode.W)) {
            player1.setCenterY(player1.getCenterY() - speed);
        }

        if (pressedKeys.contains(KeyCode.S)) {
            player1.setCenterY(player1.getCenterY() + speed);
        }

        if (pressedKeys.contains(KeyCode.A)) {
            player1.setCenterX(player1.getCenterX() - speed);
        }

        if (pressedKeys.contains(KeyCode.D)) {
            player1.setCenterX(player1.getCenterX() + speed);
        }

        // Spieler 2 (Pfeiltasten)

        if (pressedKeys.contains(KeyCode.UP)) {
            player2.setCenterY(player2.getCenterY() - speed);
        }

        if (pressedKeys.contains(KeyCode.DOWN)) {
            player2.setCenterY(player2.getCenterY() + speed);
        }

        if (pressedKeys.contains(KeyCode.LEFT)) {
            player2.setCenterX(player2.getCenterX() - speed);
        }

        if (pressedKeys.contains(KeyCode.RIGHT)) {
            player2.setCenterX(player2.getCenterX() + speed);
        }
    }

    private void handleCollision() {

        double dx = player1.getCenterX() - player2.getCenterX();
        double dy = player1.getCenterY() - player2.getCenterY();

        double distance = Math.sqrt(dx * dx + dy * dy);

        double minDistance = player1.getRadius() + player2.getRadius();

        if (distance < minDistance && distance != 0) {

            double overlap = minDistance - distance;

            double nx = dx / distance;
            double ny = dy / distance;

            double knockback = overlap / 2 + 5;

            player1.setCenterX(player1.getCenterX() + nx * knockback);
            player1.setCenterY(player1.getCenterY() + ny * knockback);

            player2.setCenterX(player2.getCenterX() - nx * knockback);
            player2.setCenterY(player2.getCenterY() - ny * knockback);
        }
    }

    private void wallCollision(Circle player) {

    double radius = player.getRadius();

    double width = player.getScene().getWidth();
    double height = player.getScene().getHeight();

    if (player.getCenterX() < radius) {
        player.setCenterX(radius);
    }

    if (player.getCenterX() > width - radius) {
        player.setCenterX(width - radius);
    }

    if (player.getCenterY() < radius) {
        player.setCenterY(radius);
    }

    if (player.getCenterY() > height - radius) {
        player.setCenterY(height - radius);
    }
}
}