module org.github.diffchecker.diffchecker {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.github.diffchecker to javafx.fxml;
    exports org.github.diffchecker;
    exports org.github.diffchecker.controllers;
    opens org.github.diffchecker.controllers to javafx.fxml;
}