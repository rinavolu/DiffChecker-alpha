package org.github.diffchecker.controllers.menu;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import org.github.diffchecker.controllers.directory.DirectoryDiffController;
import org.github.diffchecker.controllers.response.ResponseDiffController;
import org.github.diffchecker.controllers.table.TableDiffController;
import org.github.diffchecker.controllers.text.TextDiffController;
import org.github.diffchecker.model.enums.DiffType;

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
        text_diff_checker_btn.setOnAction(event -> switchToScene(DiffType.TEXT_DIFF));
        response_diff_checker_btn.setOnAction(event -> switchToScene(DiffType.RESPONSE_DIFF));
        directory_diff_checker_btn.setOnAction(event -> switchToScene(DiffType.RESPONSE_DIFF));
        table_diff_checker_btn.setOnAction(event -> switchToScene(DiffType.TABLE_DIFF));
    }

    void switchToScene(DiffType type){
        try{
            FXMLLoader loader;
            if(type.equals(DiffType.TEXT_DIFF)){

                /* Text Diff Scene Switch */
                loader = new FXMLLoader(getClass()
                        .getResource("/org/github/diffchecker/text/text-diff-view.fxml"));
                tab.setContent(loader.load());
                TextDiffController textDiffController = loader.getController();
                textDiffController.setTab(tab);
            }else if(type.equals(DiffType.RESPONSE_DIFF)){

                /* Response Diff Scene Switch */
                loader = new FXMLLoader(getClass().
                        getResource("/org/github/diffchecker/response/response-diff-view.fxml"));
                tab.setContent(loader.load());
                ResponseDiffController responseDiffController = loader.getController();
                responseDiffController.setTab(tab);
            } else if (type.equals(DiffType.DIRECTORY_DIFF)) {

                /* Directory Diff Scene Switch */
                loader = new FXMLLoader(getClass().
                        getResource("/org/github/diffchecker/directory/directory-diff-view.fxml"));
                tab.setContent(loader.load());
                DirectoryDiffController directoryDiffController = loader.getController();
                directoryDiffController.setTab(tab);
            } else if (type.equals(DiffType.TABLE_DIFF)) {

                /* Table Diff Scene Switch */
                loader = new FXMLLoader(getClass().
                        getResource("/org/github/diffchecker/table/table-diff-view.fxml"));
                tab.setContent(loader.load());
                TableDiffController tableDiffController = loader.getController();
                tableDiffController.setTab(tab);
            }
        } catch (Exception e){
                e.printStackTrace();
        }
    }

    public void setTab(Tab tab){
        this.tab = tab;
    }

}
