package com.ccyy.resourcemanager.video;


import android.graphics.Bitmap;

public class MyFile {
    private String fileName;
    private String fileSize;
    private String fileDate;
    private String filePath;
    private Bitmap bitmap;

    public MyFile(String fileName, String fileSize, String fileDate, String filePath,Bitmap bitmap) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.filePath = filePath;
        this.bitmap=bitmap;
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
    public Bitmap getBitmap(){
        return bitmap;
    }
}
