package org.github.diffchecker.controllers;

import com.google.gson.Gson;
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
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;

public class DiffCheckerController implements Initializable {

    @FXML
    public ListView<DCFile> filesList;

    @FXML
    public TabPane tabPane;

    @FXML
    public MenuBar menuBar;

    private final String CONFIG_FILE = "config.json";
    private final String CONFIG_DIR = "config";

    private ObservableList<DCFile> files;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        files = FXCollections.observableArrayList();

        //dialog box should open and display details
        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //load tabs
        loadTabsAndFiles();
        configureTabPane();
    }

    private void configureTabPane(){
        //Adding a tab to create new tabs
        Tab newTabButton = new Tab("+");
        newTabButton.setClosable(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab == newTabButton) {
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, new Tab("Untitled")); // Adding new tab before the "button" tab
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
        Config config = new Gson().fromJson(Files.readString(configFilePath), Config.class);
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
                initializeDefaultConfigFile(configFilePath);
            }else{
                System.out.println("Config file already exists");
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private void initializeDefaultConfigFile(Path configFilePath) throws IOException {
        Gson gson = new Gson();
        String configJson = gson.toJson(Config.getInstance());
        Files.write(configFilePath, configJson.getBytes(), StandardOpenOption.CREATE);
        System.out.println("Default config file created");
    }

    private void loadTabsAndFiles() {
        //For UI
        List<DCFile> activeTabs = Config.getInstance().getActiveTabs();
        tabPane.getTabs().clear();
        for(DCFile activeTab : activeTabs) {
            tabPane.getTabs().add(new Tab(activeTab.getFileName()));
        }

        files.addAll(Config.getInstance().getFiles());
        filesList.setCellFactory(param -> new ListCell<DCFile>() {
            @Override
            protected void updateItem(DCFile item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null || item.getFileName() == null) {
                    //setText(null);
                }else{
                    setText(item.getFileName());
                }
            }
        });
        filesList.setItems(files);
    }

}