package bt.ahaus.bierpong1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.HashSet;
import java.util.Set;

public class PrimaryController {

    // =====================================================
    // FXML OBJEKTE
    // Hier werden Objekte aus der FXML-Datei eingebunden.
    // =====================================================

    @FXML
    private AnchorPane gamePane;

    @FXML
    private Circle player1;

    @FXML
    private Circle player2;


    // =====================================================
    // SPIELEINSTELLUNGEN
    // Hier können Geschwindigkeit usw. geändert werden.
    // =====================================================

    // Laufgeschwindigkeit der Spieler
    private final double speed = 5;

    // Speichert aktuell gedrückte Tasten
    private final Set<KeyCode> keys = new HashSet<>();


    // =====================================================
    // INITIALISIERUNG
    // Wird automatisch beim Start ausgeführt.
    // =====================================================

    @FXML
    public void initialize() {

        player1.sceneProperty().addListener((obs, oldScene, newScene) -> {

            if (newScene != null) {

                // Taste gedrückt
                newScene.setOnKeyPressed(e ->
                        keys.add(e.getCode()));

                // Taste losgelassen
                newScene.setOnKeyReleased(e ->
                        keys.remove(e.getCode()));

                // Spiel starten
                startLoop();
            }
        });
    }


    // =====================================================
    // GAME LOOP
    // Läuft ca. 60 Mal pro Sekunde.
    // Hier können später weitere Funktionen ergänzt werden.
    // =====================================================

    private void startLoop() {

        Thread t = new Thread(() -> {

            while (true) {

                Platform.runLater(() -> {

                    // Spieler bewegen
                    movePlayers();

                    // Spieler stoßen sich ab
                    handleCollision();

                    // Kollision mit Wand
                    wallCollision(player1);
                    wallCollision(player2);

                    // HIER können später neue Funktionen rein:
                    //
                    // updateBall();
                    // updateCups();
                    // checkGoals();
                    // updateScore();
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


    // =====================================================
    // SPIELERBEWEGUNG
    //
    // Spieler 1:
    // W A S D
    //
    // Spieler 2:
    // Pfeiltasten
    // =====================================================

    private void movePlayers() {

        // Spieler 1

        if (keys.contains(KeyCode.W))
            player1.setCenterY(player1.getCenterY() - speed);

        if (keys.contains(KeyCode.S))
            player1.setCenterY(player1.getCenterY() + speed);

        if (keys.contains(KeyCode.A))
            player1.setCenterX(player1.getCenterX() - speed);

        if (keys.contains(KeyCode.D))
            player1.setCenterX(player1.getCenterX() + speed);


        // Spieler 2

        if (keys.contains(KeyCode.UP))
            player2.setCenterY(player2.getCenterY() - speed);

        if (keys.contains(KeyCode.DOWN))
            player2.setCenterY(player2.getCenterY() + speed);

        if (keys.contains(KeyCode.LEFT))
            player2.setCenterX(player2.getCenterX() - speed);

        if (keys.contains(KeyCode.RIGHT))
            player2.setCenterX(player2.getCenterX() + speed);
    }


    // =====================================================
    // SPIELERKOLLISION
    //
    // Verhindert, dass Spieler ineinander stehen.
    // =====================================================

    private void handleCollision() {

        double dx = player1.getCenterX() - player2.getCenterX();
        double dy = player1.getCenterY() - player2.getCenterY();

        double dist = Math.sqrt(dx * dx + dy * dy);

        double min = player1.getRadius() + player2.getRadius();

        if (dist < min && dist != 0) {

            // Richtung berechnen

            double nx = dx / dist;
            double ny = dy / dist;

            // Abstand wieder herstellen

            double push = (min - dist) / 2;

            player1.setCenterX(player1.getCenterX() + nx * push);
            player1.setCenterY(player1.getCenterY() + ny * push);

            player2.setCenterX(player2.getCenterX() - nx * push);
            player2.setCenterY(player2.getCenterY() - ny * push);
        }
    }


    // =====================================================
    // WANDKOLLISION
    //
    // Verhindert, dass Spieler aus dem Spielfeld laufen.
    // Funktioniert automatisch bei Fenstergrößenänderung.
    // =====================================================

    private void wallCollision(Circle p) {

        double r = p.getRadius();

        double w = gamePane.getWidth();
        double h = gamePane.getHeight();

        if (w <= 0 || h <= 0)
            return;

        if (p.getCenterX() < r)
            p.setCenterX(r);

        if (p.getCenterX() > w - r)
            p.setCenterX(w - r);

        if (p.getCenterY() < r)
            p.setCenterY(r);

        if (p.getCenterY() > h - r)
            p.setCenterY(h - r);
    }
}