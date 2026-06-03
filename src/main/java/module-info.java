module bt.ahaus.bierpong1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens bt.ahaus.bierpong1 to javafx.fxml;
    exports bt.ahaus.bierpong1;
}
