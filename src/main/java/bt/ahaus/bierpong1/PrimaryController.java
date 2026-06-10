package bt.ahaus.bierpong1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
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

    @FXML
    private ImageView cup;

    private final double speed = 5;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    // relative Position vom Becher
    private final double cupRelX = 0.7;
    private final double cupRelY = 0.5;

    @FXML
    public void initialize() {

        player1.sceneProperty().addListener((obs, oldScene, newScene) -> {

            if (newScene != null) {

                newScene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
                newScene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

                startGameLoop();
            }
        });
    }

    private void startGameLoop() {

        Thread gameThread = new Thread(() -> {

            while (true) {

                Platform.runLater(() -> {

                    movePlayers();
                    handleCollision();

                    wallCollision(player1);
                    wallCollision(player2);

                    updateCup();
                });

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gameThread.setDaemon(true);
        gameThread.start();
    }

    private void movePlayers() {

        if (pressedKeys.contains(KeyCode.W))
            player1.setCenterY(player1.getCenterY() - speed);
        if (pressedKeys.contains(KeyCode.S))
            player1.setCenterY(player1.getCenterY() + speed);
        if (pressedKeys.contains(KeyCode.A))
            player1.setCenterX(player1.getCenterX() - speed);
        if (pressedKeys.contains(KeyCode.D))
            player1.setCenterX(player1.getCenterX() + speed);

        if (pressedKeys.contains(KeyCode.UP))
            player2.setCenterY(player2.getCenterY() - speed);
        if (pressedKeys.contains(KeyCode.DOWN))
            player2.setCenterY(player2.getCenterY() + speed);
        if (pressedKeys.contains(KeyCode.LEFT))
            player2.setCenterX(player2.getCenterX() - speed);
        if (pressedKeys.contains(KeyCode.RIGHT))
            player2.setCenterX(player2.getCenterX() + speed);
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

            double push = overlap / 2 + 5;

            player1.setCenterX(player1.getCenterX() + nx * push);
            player1.setCenterY(player1.getCenterY() + ny * push);

            player2.setCenterX(player2.getCenterX() - nx * push);
            player2.setCenterY(player2.getCenterY() - ny * push);
        }
    }

    private void wallCollision(Circle player) {

        double radius = player.getRadius();

        double width = gamePane.getWidth();
        double height = gamePane.getHeight();

        if (width <= 0 || height <= 0) return;

        if (player.getCenterX() < radius)
            player.setCenterX(radius);

        if (player.getCenterX() > width - radius)
            player.setCenterX(width - radius);

        if (player.getCenterY() < radius)
            player.setCenterY(radius);

        if (player.getCenterY() > height - radius)
            player.setCenterY(height - radius);
    }

    private void updateCup() {

        double width = gamePane.getWidth();
        double height = gamePane.getHeight();

        if (width <= 0 || height <= 0) return;

        // Position bleibt relativ
        cup.setLayoutX(width * cupRelX - cup.getFitWidth() / 2);
        cup.setLayoutY(height * cupRelY - cup.getFitHeight() / 2);

        // Skalierung
        double scale = width / 1000.0;

        cup.setFitWidth(60 * scale);
        cup.setFitHeight(60 * scale);
    }
}