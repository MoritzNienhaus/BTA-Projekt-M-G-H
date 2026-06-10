package bt.ahaus.bierpong1;

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

    private final Set<KeyCode> keys = new HashSet<>();

    private GameManager game;

    @FXML
    public void initialize() {

        player1.sceneProperty().addListener((obs, oldScene, newScene) -> {

            if (newScene != null) {

                newScene.setOnKeyPressed(e -> keys.add(e.getCode()));
                newScene.setOnKeyReleased(e -> keys.remove(e.getCode()));

                // GAME START
                game = new GameManager(gamePane, player1, player2, keys);
                game.start();
            }
        });
    }
}