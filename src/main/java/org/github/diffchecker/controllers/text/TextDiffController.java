package org.github.diffchecker.controllers.text;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import org.github.diffchecker.util.DiffCheckerUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class TextDiffController implements Initializable {

    @FXML
    public MFXButton home_btn;

    private Tab tab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        home_btn.setOnAction(event -> tab.setContent(DiffCheckerUtil.getMenuViewNode(tab)));
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

}
