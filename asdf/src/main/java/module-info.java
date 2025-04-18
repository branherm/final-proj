module edu.guilford {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.

    opens edu.guilford to javafx.fxml;
    exports edu.guilford;
}