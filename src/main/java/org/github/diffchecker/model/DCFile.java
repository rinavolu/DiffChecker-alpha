package org.github.diffchecker.model;

import java.time.Instant;

public class DCFile {

    private String fileName;

    private String filePath;

    private String creationDate;

    private boolean isActive;

    public DCFile() {
        String currentTimeInMillis = String.valueOf(System.currentTimeMillis());
        this.creationDate = Instant.now().toString();
        this.isActive = true;
        this.fileName = "Untitled_"+currentTimeInMillis;
        this.filePath = "";
    }

    public DCFile(String fileName, String filePath, boolean isActive) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.creationDate = Instant.now().toString();
        this.isActive = isActive;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
