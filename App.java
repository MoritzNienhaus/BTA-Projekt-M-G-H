
package com.mycompany.bierpong;

// Importiert JavaFX Klassen
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Hauptklasse des Programms
public class App extends Application {

    /*
    Diese Methode startet automatisch
    beim Programmstart.
    */
    @Override
    public void start(Stage stage) throws Exception {

        /*
        Lädt die Datei primary.fxml.

        FXMLLoader erstellt daraus
        die sichtbare Oberfläche.
        */
        Parent root = FXMLLoader.load(
                App.class.getResource("primary.fxml")
        );

        /*
        Erstellt die Szene.

        root = Inhalt
        1000 x 700 = Fenstergröße
        */
        Scene scene = new Scene(root, 1000, 700);

        // Titel des Fensters
        stage.setTitle("Bier Pong Game");

        // Setzt die Szene
        stage.setScene(scene);

        // Zeigt das Fenster an
        stage.show();
    }

    /*
    Startpunkt des Programms.
    */
    public static void main(String[] args) {

        // Startet JavaFX
        launch();
    }
}
