package org.github.diffchecker.controllers.menu;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TabMenuController implements Initializable {

    @FXML
    public MFXButton text_diff_checker_btn;

    @FXML
    public MFXButton response_diff_checker_btn;

    @FXML
    public MFXButton directory_diff_checker_btn;

    @FXML
    public MFXButton table_diff_checker_btn;

    Tab tab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBtnClickHandlers();
    }

    void addBtnClickHandlers(){
        text_diff_checker_btn.setOnAction(event -> switchToScene("text"));
        response_diff_checker_btn.setOnAction(event -> switchToScene("response"));
        directory_diff_checker_btn.setOnAction(event -> switchToScene("directory"));
        table_diff_checker_btn.setOnAction(event -> switchToScene("table"));
    }

    void switchToScene(String type){
        try{
            if(type.equals("text")){
                HBox text_diff_checker_node = new FXMLLoader(getClass()
                        .getResource("/org/github/diffchecker/text/text-diff-view.fxml")).load();
                tab.setContent(text_diff_checker_node);
            }else if(type.equals("response")){
                tab.setContent(new FXMLLoader(getClass().
                        getResource("/org/github/diffchecker/response/response-diff-view.fxml")).load());
            } else if (type.equals("directory")) {
                tab.setContent(new FXMLLoader(getClass().
                        getResource("/org/github/diffchecker/directory/directory-diff-view.fxml")).load());
            } else if (type.equals("table")) {
                tab.setContent(new FXMLLoader(getClass().
                        getResource("/org/github/diffchecker/table/table-diff-view.fxml")).load());
            }
        } catch (Exception e){
                e.printStackTrace();
        }
    }

    public void setTab(Tab tab){
        this.tab = tab;
    }

}
