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
    exports org.github.diffchecker.controllers.text;
    exports org.github.diffchecker.controllers.response;
    exports org.github.diffchecker.controllers.directory;
    exports org.github.diffchecker.controllers.table;
    exports org.github.diffchecker.util;
    opens org.github.diffchecker.model to com.google.gson;
    opens org.github.diffchecker.controllers to javafx.fxml;
}