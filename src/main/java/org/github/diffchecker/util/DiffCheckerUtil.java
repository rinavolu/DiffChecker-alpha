package org.github.diffchecker.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.github.diffchecker.controllers.menu.TabMenuController;

public class DiffCheckerUtil {

    public static VBox getMenuViewNode(Tab tab){
        VBox menuView = null;
        try {
            FXMLLoader loader = new FXMLLoader(DiffCheckerUtil.class.getResource("/org/github/diffchecker/menu/tab-menu-view.fxml"));
            menuView = loader.load();
            TabMenuController controller = loader.getController();
            controller.setTab(tab);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return menuView;
    }
}
