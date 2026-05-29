
package com.mycompany.bierpong;

// JavaFX Imports
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

// Java Standardbibliothek
import java.util.HashSet;
import java.util.Set;

// Controller für primary.fxml
public class PrimaryController {

    /*
    Verbindet die Kreise aus der FXML
    mit Java Variablen.
    */
    @FXML
    private Circle player1;

    @FXML
    private Circle player2;

    // Bewegungsgeschwindigkeit
    private final double speed = 5;

    /*
    Speichert aktuell gedrückte Tasten.
    */
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    /*
    initialize() wird automatisch ausgeführt,
    nachdem die FXML geladen wurde.
    */
    @FXML
    public void initialize() {

        /*
        Wartet darauf,
        dass die Scene existiert.
        */
        player1.sceneProperty().addListener((obs, oldScene, newScene) -> {

            // Prüft ob Scene vorhanden
            if (newScene != null) {

                Scene scene = newScene;

                /*
                Wird Taste gedrückt:
                Taste speichern.
                */
                scene.setOnKeyPressed(event ->
                        pressedKeys.add(event.getCode())
                );

                /*
                Wird Taste losgelassen:
                Taste entfernen.
                */
                scene.setOnKeyReleased(event ->
                        pressedKeys.remove(event.getCode())
                );

                // Startet die Spielschleife
                startGameLoop();
            }
        });
    }

    /*
    Haupt-Spielschleife.
    Läuft dauerhaft im Hintergrund.
    */
    private void startGameLoop() {

        // Neuer Thread
        Thread gameThread = new Thread(() -> {

            try {

                // Endlosschleife
                while (true) {

                    /*
                    runLater führt Code
                    sicher im JavaFX Thread aus.
                    */
                    javafx.application.Platform.runLater(() -> {

                        // Spieler bewegen
                        movePlayers();

                        // Spieler-Kollision prüfen
                        handleCollision();

                        // Wandkollision Spieler 1
                        wallCollision(player1);

                        // Wandkollision Spieler 2
                        wallCollision(player2);
                    });

                    /*
                    16ms Pause
                    ≈ 60 FPS
                    */
                    Thread.sleep(16);
                }

            } catch (InterruptedException e) {

                // Fehlerausgabe
                e.printStackTrace();
            }
        });

        /*
        Daemon Thread beendet sich automatisch,
        wenn das Programm geschlossen wird.
        */
        gameThread.setDaemon(true);

        // Thread starten
        gameThread.start();
    }

    /*
    Bewegt beide Spieler.
    */
    private void movePlayers() {

        // Spieler 1 -> WASD

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

        // Spieler 2 -> Pfeiltasten

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

    /*
    Prüft Kollision zwischen beiden Spielern.
    */
    private void handleCollision() {

        // Abstand auf X und Y Achse
        double dx = player1.getCenterX() - player2.getCenterX();
        double dy = player1.getCenterY() - player2.getCenterY();

        // Echter Abstand zwischen Spielern
        double distance = Math.sqrt(dx * dx + dy * dy);

        /*
        Minimaler Abstand,
        bevor sich die Kreise berühren.
        */
        double minDistance =
                player1.getRadius() + player2.getRadius();

        // Kollision erkannt
        if (distance < minDistance) {

            // Überschneidung berechnen
            double overlap = minDistance - distance;

            // Richtungsvektor berechnen
            double nx = dx / distance;
            double ny = dy / distance;

            // Stärke des Knockbacks
            double knockback = overlap / 2 + 5;

            // Spieler 1 wegschieben
            player1.setCenterX(
                    player1.getCenterX() + nx * knockback
            );

            player1.setCenterY(
                    player1.getCenterY() + ny * knockback
            );

            // Spieler 2 wegschieben
            player2.setCenterX(
                    player2.getCenterX() - nx * knockback
            );

            player2.setCenterY(
                    player2.getCenterY() - ny * knockback
            );
        }
    }

    /*
    Verhindert,
    dass Spieler das Spielfeld verlassen.
    */
    private void wallCollision(Circle player) {

        double radius = player.getRadius();

        double width = 1000;
        double height = 700;

        // Linke Wand
        if (player.getCenterX() < radius) {
            player.setCenterX(radius + 10);
        }

        // Rechte Wand
        if (player.getCenterX() > width - radius) {
            player.setCenterX(width - radius - 10);
        }

        // Obere Wand
        if (player.getCenterY() < radius) {
            player.setCenterY(radius + 10);
        }

        // Untere Wand
        if (player.getCenterY() > height - radius) {
            player.setCenterY(height - radius - 10);
        }
    }
}
