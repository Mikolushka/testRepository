module com.example.motel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;


    opens com.example.motel to javafx.fxml;
    exports com.example.motel;
}