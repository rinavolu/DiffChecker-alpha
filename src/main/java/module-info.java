module org.github.diffchecker.diffchecker {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires com.dlsc.gemsfx;
    requires com.google.gson;
    requires atlantafx.base;


    opens org.github.diffchecker to javafx.fxml;
    exports org.github.diffchecker.model;
    exports org.github.diffchecker;
    exports org.github.diffchecker.controllers;
    exports org.github.diffchecker.controllers.menu;
    opens org.github.diffchecker.model to com.google.gson;
    opens org.github.diffchecker.controllers to javafx.fxml;
}