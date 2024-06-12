package org.github.diffchecker.controllers;

import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.github.diffchecker.model.Config;
import org.github.diffchecker.model.DCFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class DiffCheckerController implements Initializable {

    @FXML
    public ListView<DCFile> filesListView;

    @FXML
    public TabPane tabPane;

    @FXML
    public MenuBar menuBar;

    //Menu
    @FXML
    public MenuItem saveBtn;


    private final String CONFIG_FILE = "config.json";
    private final String CONFIG_DIR = "config";

    private ObservableList<DCFile> obsFilesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Config
        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //load tabs
        loadTabsAndFiles();
        configureTabPane();

        //Menu
        initializeMenuItems();

    }

    private void configureTabPane(){
        //Adding a tab to create new tabs
        Tab newTabButton = new Tab("+");
        newTabButton.setClosable(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab == newTabButton) {
                Tab tab = createDefaultTab();
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab); // Adding new tab before the "button" tab
                tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2); // Selecting the tab before the button, which is the newly created one
            }
        });
        tabPane.getTabs().add(newTabButton);
    }

    private void loadConfig() throws IOException {

        Path configDirPath = Paths.get(CONFIG_DIR);
        Path configFilePath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        //create config dir & file if doesn't exists
        createConfigIfDoesNotExist(configDirPath, configFilePath);
        //read config file
        Config config = new GsonBuilder().setLenient().create().fromJson(Files.readString(configFilePath), Config.class);
        Config.getInstance().setConfig(config);
    }

    private void createConfigIfDoesNotExist(Path configDirPath, Path configFilePath) throws IOException {
        try {
            if (!Files.exists(configDirPath)) {
                System.out.println("Config directory does not exist, creating it");
                Files.createDirectory(configDirPath);
            }

            if (!Files.exists(configFilePath)) {
                System.out.println("Config file does not exist, creating it");
                Files.createFile(configFilePath);
                //Initialize Config
                Config.getInstance().saveConfig(configFilePath);
                //initializeDefaultConfigFile(configFilePath);
            }else{
                System.out.println("Config file already exists");
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private void loadTabsAndFiles() {
        this.obsFilesList = FXCollections.observableArrayList();
        //For UI
        List<DCFile> activeTabs = Config.getInstance().getActiveTabs();
        tabPane.getTabs().clear();
        for(DCFile activeTab : activeTabs) {
            Tab tab = createTab(activeTab.getFileName());
            tabPane.getTabs().add(tab);
        }
        filesListView.setCellFactory(param -> {
            ListCell<DCFile> cell = new ListCell<>() {
                @Override
                protected void updateItem(DCFile item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null || item.getFileName() == null) {
                        //setText(null);
                    }else{
                        setText(item.getFileName());
                    }
                }
            };
            ContextMenu contextMenu = new ContextMenu();
            MenuItem openItem = new MenuItem("Open");
            openItem.setOnAction(event -> openFileInTabPane(cell.getText()));

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> closeAndDeleteTab(cell.getText()));
            contextMenu.getItems().addAll(openItem, deleteItem);
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });

            return cell;
        });
        obsFilesList.setAll(Config.getInstance().getFiles());
        filesListView.setItems(obsFilesList);
    }

    private void initializeMenuItems(){
        saveBtn.setOnAction(event -> {
            try {
                saveTabs();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void saveTabs() throws IOException {
         ObservableList<Tab> tabs = tabPane.getTabs();
         for(Tab tab : tabs) {
             if(!tab.getText().equals("+")) {
                 if(isFileExists(tab.getText())) {
                     System.out.println("File already exists, Ignoring");
                 }else{
                     DCFile file = new DCFile(tab.getText(), "", true);
                     obsFilesList.add(file);
                     Config.getInstance().addFile(file);
                 }
             }
         }
         Config.getInstance().saveConfig(Paths.get(CONFIG_DIR, CONFIG_FILE));
    }

    private boolean isFileExists(String fileName) {
        for(DCFile file : obsFilesList) {
            if(file.getFileName().equals(fileName)){
                return true;
            }
        }
        return false;
    }

    private DCFile getDCFile(String fileName) {
        for(DCFile file : obsFilesList) {
            if(file.getFileName().equals(fileName)){
                return file;
            }
        }
        return null;
    }

    private void openFileInTabPane(String fileName) {
        if(isFileExists(fileName)){
            DCFile file = getDCFile(fileName);
            if(!file.isActive()){
                createAndAddTab(fileName);
                Config.getInstance().changeTabStatus(fileName, true);
            } else{
                //System.out.println("Tab already exists, selecting tab");
                tabPane.getSelectionModel().select(getTabIndex(fileName));
            }
        }
    }

    private void createAndAddTab(String fileName){
        Tab tab = createTab(fileName);
        tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
        tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
    }

    private Tab createTab(String fileName){
        Tab tab = new Tab(fileName);
        tab.setOnClosed(event -> Config.getInstance().changeTabStatus(tab.getText(), false));
        return tab;
    }

    private Tab createDefaultTab(){
        Tab tab = new Tab("Untitled_"+System.currentTimeMillis());
        tab.setOnClosed(event -> Config.getInstance().changeTabStatus(tab.getText(), false));
        return tab;
    }

    private int getTabIndex(String fileName){
        ObservableList<Tab> tabs = tabPane.getTabs();
        for(int index=0; index < tabs.size(); index++){
            if(tabs.get(index).getText().equals(fileName)){
                return index;
            }
        }
        return -1;
    }

    private int getObsListIndex(String fileName){
        for(int index=0; index < obsFilesList.size(); index++){
            if(obsFilesList.get(index).getFileName().equals(fileName)){
                return index;
            }
        }
        return -1;
    }

    private void closeAndDeleteTab(String fileName){
        int tabIndex = getTabIndex(fileName);
        if(tabIndex != -1){
            tabPane.getTabs().remove(tabIndex);
        }
        obsFilesList.remove(getObsListIndex(fileName));
        filesListView.refresh();
        Config.getInstance().removeFile(fileName);
    }
}