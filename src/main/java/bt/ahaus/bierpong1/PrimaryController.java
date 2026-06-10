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

    @FXML private AnchorPane gamePane;

    @FXML private Circle player1;
    @FXML private Circle player2;

    @FXML private ImageView cupL1, cupL2, cupL3, cupL4, cupL5, cupL6;
    @FXML private ImageView cupR1, cupR2, cupR3, cupR4, cupR5, cupR6;

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
                    updateCups();
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

            double overlap = min - dist;

            double nx = dx / dist;
            double ny = dy / dist;

            double push = overlap / 2 + 5;

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

    private void updateCups() {

        double w = gamePane.getWidth();
        double h = gamePane.getHeight();

        if (w <= 0 || h <= 0) return;

        double scale = w / 1000.0;
        double size = 60 * scale;

        // LINKS
        place(cupL1, w * 0.15, h * 0.35, size);
        place(cupL2, w * 0.12, h * 0.45, size);
        place(cupL3, w * 0.09, h * 0.55, size);
        place(cupL4, w * 0.18, h * 0.45, size);
        place(cupL5, w * 0.15, h * 0.55, size);
        place(cupL6, w * 0.12, h * 0.65, size);

        // RECHTS
        place(cupR1, w * 0.85, h * 0.35, size);
        place(cupR2, w * 0.88, h * 0.45, size);
        place(cupR3, w * 0.91, h * 0.55, size);
        place(cupR4, w * 0.82, h * 0.45, size);
        place(cupR5, w * 0.85, h * 0.55, size);
        place(cupR6, w * 0.88, h * 0.65, size);
    }

    private void place(ImageView c, double x, double y, double size) {

        c.setFitWidth(size);
        c.setFitHeight(size);

        c.setLayoutX(x - size / 2);
        c.setLayoutY(y - size / 2);
    }
}