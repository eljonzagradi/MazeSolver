module com.pathfinder.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.logging;

    opens com to javafx.fxml;
    exports com;
}