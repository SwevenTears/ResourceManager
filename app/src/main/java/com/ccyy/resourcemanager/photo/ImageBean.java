package com.ccyy.resourcemanager.photo;

import java.io.Serializable;

public class ImageBean implements Serializable{
    private String imgPath;
    private boolean isChoosed;
    private int top;

    public ImageBean(String imgPath, boolean isChoosed) {
        this.imgPath = imgPath;
        this.isChoosed = isChoosed;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public int getTop() {
        return top;
    }
}