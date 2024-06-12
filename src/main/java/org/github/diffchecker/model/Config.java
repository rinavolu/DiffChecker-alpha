package org.github.diffchecker.model;

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
        this.files = config.getFiles();
    }

    private List<DCFile> files;

    private Config() {
        this.files = new ArrayList<>();
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
}
