package com.ccyy.resourcemanager.video;


public class MyFile {
    private String fileName;
    private String fileSize;
    private String fileDate;
    private String filePath;

    public MyFile(String fileName, String fileSize, String fileDate, String filePath) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public String getFilePath() {
        return filePath;
    }
}
