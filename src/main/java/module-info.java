module com.example.specialprojectreal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.specialprojectreal to javafx.fxml;
    exports com.example.specialprojectreal;
}