module edu.guilford {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;  // Add this line

    opens edu.guilford to javafx.fxml;
    exports edu.guilford;
}