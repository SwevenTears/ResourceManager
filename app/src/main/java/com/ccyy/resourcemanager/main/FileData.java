package com.ccyy.resourcemanager.main;

import java.util.Date;

/**
 * Created by Sweven on 2018/6/13.
 * Email:sweventears@Foxmail.com
 */
public class FileData {
    public String name;
    public String  path;
    public long last_date;
    public long size;
    public int folder_count;
    public int file_count;

    /**
     * @param name 文件或文件夹名称
     * @param path 文件或文件夹地址
     */
    public FileData(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * @param last_date 最后修改时间
     * @param size 文件大小
     * @param folder_count 子文件夹数量
     * @param file_count 子文件数量
     */
    public FileData(long last_date, long size, int folder_count, int file_count) {
        this.last_date = last_date;
        this.size = size;
        this.folder_count = folder_count;
        this.file_count = file_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLast_date() {
        return last_date;
    }

    public void setLast_date(long last_date) {
        this.last_date = last_date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getFolder_count() {
        return folder_count;
    }

    public void setFolder_count(int folder_count) {
        this.folder_count = folder_count;
    }

    public int getFile_count() {
        return file_count;
    }

    public void setFile_count(int file_count) {
        this.file_count = file_count;
    }
}
