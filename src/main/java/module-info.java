module com.example.pizza_ordering_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.pizza_ordering_system to javafx.fxml;
    exports com.example.pizza_ordering_system;
}