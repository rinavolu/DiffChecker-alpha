package org.github.diffchecker.model;

import com.google.gson.Gson;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Config {


    private static Config config;


    public static synchronized Config getInstance(){
        if(config == null){
            config = new Config();
        }
        return config;
    }

    public void setConfig(Config config){
        this.files.clear();
        this.files.addAll(config.getFiles());
    }

    private List<DCFile> files;

    private Config() {
        this.files = FXCollections.observableArrayList();
        this.files.add(new DCFile());
    }

    public List<DCFile> getFiles() {
        return files;
    }

    public List<String> getFileNames(){
        List<String> fileNames = new ArrayList<>();
        for(DCFile file : files){
            fileNames.add(file.getFileName());
        }
        return fileNames;
    }

    public List<DCFile> getActiveTabs(){
        List<DCFile> activeTabs = new ArrayList<>();
        for(DCFile file : files){
            if(file.isActive()){
                activeTabs.add(file);
            }
        }
        return activeTabs;
    }

    public void addFile(DCFile file){
        files.add(file);
    }

    public void removeFile(String fileName){
        for(DCFile file : files){
            if(file.getFileName().equals(fileName)){
                files.remove(file);
                break;
            }
        }
    }

    public void changeTabStatus(String fileName, boolean active){
        for(DCFile file : files){
            if(file.getFileName().equals(fileName)){
                file.setActive(active);
            }
        }
    }

    public void saveConfig(Path configFilePath) throws IOException {
        Gson gson = new Gson();
        String configJson = gson.toJson(Config.getInstance());
        Files.write(configFilePath, configJson.getBytes() );
    }
}
