module com.example.tankbattle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.tankbattle to javafx.fxml;
    exports com.example.tankbattle;
    exports com.example.tankbattle.control;
    opens com.example.tankbattle.control to javafx.fxml;
    exports com.example.tankbattle.model;
}