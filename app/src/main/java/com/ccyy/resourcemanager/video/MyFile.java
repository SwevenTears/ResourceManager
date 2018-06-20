package com.ccyy.resourcemanager.video;


public class MyFile {
    private int imageId;
    private String fileName;
    private String fileSize;
    private String filePower;
    private String fileDate;

    public MyFile(String fileName,int imageId,String fileSize,String filePower,String fileDate){
        super();
        this.fileName=fileName;
        this.imageId=imageId;
        this.fileSize=fileSize;
        this.filePower=filePower;
        this.fileDate=fileDate;
    }

    public String getFileName() {
        return fileName;
    }
    public int getImageId(){
        return imageId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFilePower() {
        return filePower;
    }

    public String getFileDate() {
        return fileDate;
    }
}
