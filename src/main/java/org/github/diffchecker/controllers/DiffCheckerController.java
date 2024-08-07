package org.github.diffchecker.controllers;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.StringConverter;
import org.github.diffchecker.factory.DCFileCellFactory;
import org.github.diffchecker.model.Config;
import org.github.diffchecker.model.DCFile;
import org.github.diffchecker.model.enums.ContextMenuType;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class DiffCheckerController implements Initializable {

    @FXML
    public MFXListView<DCFile> filesListView;

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
        addNewTabButtonToTabPane();
        initialSelectionInFilesListView();

        //Menu
        initializeMenuItems();

    }

    private void addNewTabButtonToTabPane(){
        //Adding a tab to create new tabs
        Tab newTabButton = new Tab("+");
        newTabButton.setClosable(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab == newTabButton) {
                Tab tab = createDefaultTab();
                tab.setContextMenu(getContextMenu(ContextMenuType.TAB_CONTEXT_MENU));
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
        Config.getInstance().readConfig(configFilePath);
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
            tab.setContextMenu(getContextMenu(ContextMenuType.TAB_CONTEXT_MENU));
            tabPane.getTabs().add(tab);
        }
        StringConverter<DCFile> converter = FunctionalStringConverter.to(file -> (file == null) ? "" : file.getFileName());
        filesListView.setCellFactory(dcFile -> new DCFileCellFactory(filesListView, dcFile));
        filesListView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && (mouseEvent.getClickCount() == 2)){
                openSelectedFilesInTabPane();
            }
        });
        filesListView.setConverter(converter);
        obsFilesList.setAll(Config.getInstance().getFiles());
        filesListView.setItems(obsFilesList);
        filesListView.setContextMenu(getContextMenu(ContextMenuType.FILES_LIST_CONTEXT_MENU));
    }

    private void initializeMenuItems(){
        menuBar.setUseSystemMenuBar(true);
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
                addFileToTabPane(fileName);
                Config.getInstance().changeTabStatus(fileName, true);
            } else{
                //System.out.println("Tab already exists, selecting tab");
                tabPane.getSelectionModel().select(getTabIndex(fileName));
            }
        }
    }

    private void openSelectedFilesInTabPane(){
        List<DCFile> filesToOpen = filesListView.getSelectionModel().getSelection().values().stream().toList();
        for(DCFile fileToOpen : filesToOpen) {
            if(isFileExists(fileToOpen.getFileName())){
                if(!fileToOpen.isActive()){
                    addFileToTabPane(fileToOpen.getFileName());
                    Config.getInstance().changeTabStatus(fileToOpen.getFileName(), true);
                } else{
                    tabPane.getSelectionModel().select(getTabIndex(fileToOpen.getFileName()));
                }
            }
        }
    }

    private void addFileToTabPane(String fileName){
        Tab tab = createTab(fileName);
        tab.setContextMenu(getContextMenu(ContextMenuType.TAB_CONTEXT_MENU));
        tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
        tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
    }

    private Tab createTab(String fileName){
        Tab tab = new Tab(fileName);
        tab.setOnSelectionChanged(event -> changeSelectionInFilesListView(tab.getText()));
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
        Config.getInstance().removeFile(fileName);
    }

    private void closeAndDeleteFiles(List<DCFile> filesToClose){
        for(DCFile fileToClose : filesToClose){
            String fileName = fileToClose.getFileName();
            int tabIndex = getTabIndex(fileName);
            if(tabIndex != -1){
                tabPane.getTabs().remove(tabIndex);
            }
            obsFilesList.remove(getObsListIndex(fileName));
            Config.getInstance().removeFile(fileName);
        }
    }

    private void closeTab(){
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if(tab != null){
            Config.getInstance().changeTabStatus(tab.getText(), false);
            tabPane.getTabs().remove(tab);
        }
    }

    private void initialSelectionInFilesListView(){
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        if(tab != null){
            changeSelectionInFilesListView(tab.getText());
        }
    }

    private void changeSelectionInFilesListView(String fileName){
        DCFile file = getDCFile(fileName);
        if(file != null){
            filesListView.getSelectionModel().clearSelection();
            filesListView.getSelectionModel().selectItem(file);
        }
    }

    private ContextMenu getContextMenu(ContextMenuType contextMenuType){
        ContextMenu contextMenu = new ContextMenu();
        if(contextMenuType.equals(ContextMenuType.TAB_CONTEXT_MENU)){
            //Context Menu for tab
            MenuItem saveItem = new MenuItem("Save");

            MenuItem closeItem = new MenuItem("Close");
            closeItem.setOnAction(event -> closeTab());

            MenuItem renameItem = new MenuItem("Rename");
            MenuItem deleteItem = new MenuItem("Delete");
            contextMenu.getItems().addAll(saveItem, renameItem,closeItem, deleteItem);
        }
        else if(contextMenuType.equals(ContextMenuType.FILES_LIST_CONTEXT_MENU)){
            MenuItem openItem = new MenuItem("Open");
            openItem.setOnAction(event -> openSelectedFilesInTabPane());

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event ->
                    closeAndDeleteFiles(filesListView.getSelectionModel().getSelection().values().stream().toList()));
            contextMenu.getItems().addAll(openItem, deleteItem);
        }

        return contextMenu;
    }
}