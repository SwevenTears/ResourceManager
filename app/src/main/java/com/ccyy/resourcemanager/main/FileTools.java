package com.ccyy.resourcemanager.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.ccyy.resourcemanager.MainActivity;
import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.ResourceManager;
import com.ccyy.resourcemanager.music.PlayActivity;
import com.ccyy.resourcemanager.text.EditTextActivity;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.FileType;
import com.ccyy.resourcemanager.tools.T;
import com.ccyy.resourcemanager.video.PlayVideoActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.jar.JarFile;

/**
 * Created by Sweven on 2018/6/18.
 * Email:sweventears@Foxmail.com
 */
public class FileTools {

    private static Bitmap folder_previous;
    private static Bitmap folder_icon;
    private static Bitmap bitmap_image;
    private static Bitmap bitmap_video;
    private static Bitmap bitmap_music;
    private static Bitmap bitmap_text;
    private static Bitmap unknown_file;

    private static String rootPath;

    private static T t;

    /**
     * @param context
     * @param rootPath 设置文件图标地址、传递 rootPath
     */
    public FileTools(Context context, String rootPath) {

        FileTools.rootPath = rootPath;

        folder_previous = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.folder_previous);

        folder_icon = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.folder);

        bitmap_image = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.image);

        bitmap_video = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.video);

        bitmap_music = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.music);

        bitmap_text = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.text);

        unknown_file = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.file_unknown);

        t = new T(context);
    }

    /**
     * 取得文件架构的method
     *
     * @param filePath 文件当前目录
     * @return 当前目录的文件列表
     */
    public static ArrayList<FileData> getFileList(String filePath) {


        File f = new File(filePath);
        //找到f下的所有文件的列表
        File[] files = f.listFiles();

        ArrayList<FileData> allFolder = new ArrayList<>(), allFile = new ArrayList<>();

        boolean isRoot = false;

        if (!filePath.equals(rootPath)) {
            /* 设定为[返回上一级] */
            FileData fileData = new FileData("<<previous>>", f.getPath(), folder_previous,
                    0, 0, false);
            allFolder.add(fileData);
            isRoot = true;
        }
        /* 将所有文件存入ArrayList中 */
        for (File temp : files) {

            if (!temp.isHidden()) {
                String temp_path = temp.getPath();
                String temp_name = temp.getName();
                long temp_last_data = temp.lastModified();

                if (temp.isDirectory()) {//是否是文件夹

                    FileData fileData = new FileData(temp_name, temp_path, folder_icon,
                            temp_last_data, 0, false);
                    allFolder.add(fileData);
                } else {
                    long temp_size = temp.length();

                    //设置图像文件图标
                    if (FileType.isImageFileType(temp_path)) {
                        try {
                            Bitmap bitmap;
                            try {
                                bitmap = BitmapFactory.decodeFile(ResourceManager.App_Temp_Image_Path + "/" + temp_name);
                            } catch (Exception e) {
                                bitmap = BitmapFactory.decodeFile(temp_path);
                                bitmap = FileOperation.setImgSize(temp_name, bitmap, 80, 100);
                            }

                            FileData fileData = new FileData(temp_name, temp_path, bitmap,
                                    temp_last_data, temp_size, false);
                            allFile.add(fileData);
                        } catch (Exception e) {
                            //默认图标
                            FileData fileData = new FileData(temp_name, temp_path, bitmap_image,
                                    temp_last_data, temp_size, false);
                            allFile.add(fileData);
                        }
                    }

                    //设置音频文件的图标
                    else if (FileType.isAudioFileType(temp_path)) {
                        //默认图标
                        FileData fileData = new FileData(temp_name, temp_path, bitmap_music,
                                temp_last_data, temp_size, false);
                        allFile.add(fileData);
                    }

                    // 设置视频文件的图标
                    else if (FileType.isVideoFileType(temp_path)) {
                        try {
                            Bitmap bitmap;
                            // 获取视频的缩略图
                            bitmap = ThumbnailUtils.createVideoThumbnail(temp_path, MediaStore.Images.Thumbnails.MICRO_KIND);
                            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 80, 100, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                            FileData fileData = new FileData(temp_name, temp_path, bitmap,
                                    temp_last_data, temp_size, false);
                            allFile.add(fileData);
                        } catch (Exception e) {
                            //默认图标
                            FileData fileData = new FileData(temp_name, temp_path, bitmap_video,
                                    temp_last_data, temp_size, false);
                            allFile.add(fileData);
                        }
                    }

                    // 设置文本文档文件的图标
                    else if (FileType.isTextFileType(temp_path)) {
                        //默认图标
                        FileData fileData = new FileData(temp_name, temp_path, bitmap_text,
                                temp_last_data, temp_size, false);
                        allFile.add(fileData);

                    }

                    // 设置未知文件的图标
                    else {
                        FileData fileData = new FileData(temp_name, temp_path, unknown_file,
                                temp_last_data, temp_size, false);
                        allFile.add(fileData);
                    }
                }
            }
        }
        ArrayList<FileData> order_allFolder = FileOperation.order(allFolder, isRoot);
        ArrayList<FileData> order_allFile = FileOperation.order(allFile, false);

        ArrayList<FileData> order_file = order_allFolder;
        order_file.addAll(order_allFile);

        return order_file;
    }

    /**
     * @param allFile 父目录中的文件集合
     * @param file    需要查询位置的文件
     * @return 位置 （int）
     */
    public static int getPositionInParentFolder(ArrayList<FileData> allFile, File file) {
        String name = file.getName();
        ArrayList<String> folder_names = new ArrayList<>();
        for (int i = 0; i < allFile.size(); i++)
            folder_names.add(allFile.get(i).getName());
        int previous_position = FileOperation.find_folder_position(name, folder_names);

        return previous_position;
    }

    /**
     * @param path 文件地址
     * @return intent 将文件分享到其他应用
     */
    public static Intent shareSingleFile(String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_SUBJECT, "分享");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        return share;
    }

    /**
     * 删除文件操作
     *
     * @param path 文件路径
     * @return 是否修改成功
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            int count = 0;
            File childFile = null;
            for (File child : file.listFiles()) {
                count++;
                childFile = child;
            }
            if (count == 0) {
                return file.delete();
            } else {
                //todo 暂不支持删除有文件的目录
                return file.delete();
            }

        } else {
            return file.delete();
        }
    }

    /**
     * 复制文件操作
     *
     * @param filePath 文件路径
     * @param path     目标目录地址
     * @return 是否修改成功
     */
    public static boolean copyFile(String filePath, String path) {
        File file = new File(filePath);
        String fileName = file.getName();
        String newFolder = path + "/" + fileName;
        File toFile = new File(newFolder);
        try {
            FileInputStream ins = new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }
            ins.close();
            out.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 剪切文件操作
     *
     * @param filePath  原文件路径
     * @param newFolder 新文件目录下
     * @return 是否修改成功
     */
    public static boolean cutFile(String filePath, String newFolder) {
        return copyFile(filePath, newFolder) && deleteFile(filePath);
    }

    /**
     * 重命名文件操作
     *
     * @param path     文件路径
     * @param new_name 新的文件名
     * @return 是否修改成功
     */
    public static boolean renameFile(String path, String new_name) {
        File file = new File(path);
        String directory = file.getParent();
        File newFile = new File(directory + "/" + new_name);
        return file.renameTo(newFile);
    }

    /**
     * 创建文件夹操作
     *
     * @param dir         目录
     * @param folder_name 文件名
     * @return 是否创建成功
     */
    public static boolean createNewFolder(String dir, String folder_name) {
        File file = new File(dir + "/" + folder_name);
        if (!file.exists() && !file.isDirectory()) {
            return file.mkdir();
        } else {
            return false;
        }
    }

    /**
     * 判断目录下是否有同一文件名的文件
     *
     * @param dir  目录
     * @param path 文件路径
     * @return 是否有同名文件
     */
    public static boolean isSameFile_inDir(String dir, String path) {
        File dir_folder = new File(dir);
        int count = 0;
        for (String file : dir_folder.list()) {
            if (file.equals(path)) {
                count++;
            }
        }
        return count <= 0;
    }

    /**
     * 保存TXT文件
     *
     * @param newText   新写的内容
     * @param file_path 保存路径
     * @return 是否保存成功
     */
    public static boolean saveTXTFile(String newText, String file_path) {
        File file = new File(file_path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            /* 写入Txt文件 */
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(newText);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 打开已知类型文件
     *
     * @param file    File文件
     * @param context ，
     */
    public static void openFile(File file, Context context) {
        String path = file.getPath();
        String name = file.getName();
        try {
            // 打开图像文件
            if (FileType.isImageFileType(path)) {
                //todo 连接方法
            }

            // 打开音频文件
            else if (FileType.isAudioFileType(path)) {
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("path", path);
                context.startActivity(intent);
            }

            // 打开视频文件
            else if (FileType.isVideoFileType(path)) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("fileName", name);
                intent.putExtra("filePath", path);
                context.startActivity(intent);
            }

            // 打开文档文件
            else if (FileType.isTextFileType(path)) {
                Intent intent = new Intent(context, EditTextActivity.class);
                intent.putExtra("File_Name", name);
                intent.putExtra("File_Path", path);
                context.startActivity(intent);
            }

            // 打开未知文件
            else {
                t.error("未知文件，无法打开");
            }
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            String type = FileTools.getFileType(file);
            if (!type.equals("*")) {
                intent.setType(type + "/*");
                context.startActivity(Intent.createChooser(intent, "选择程序"));
                t.error("暂时无法用本软件打开文件");
            }
        }
    }

    /**
     * 获取文件mime类型
     *
     * @param file File文件
     * @return 文件的mime类型
     */
    public static String getFileType(File file) {
        String path = file.getPath();
        if (FileType.isImageFileType(path)) {
            return "image";
        } else if (FileType.isAudioFileType(path)) {
            return "audio";
        } else if (FileType.isVideoFileType(path)) {
            return "video";
        } else if (FileType.isTextFileType(path)) {
            return "text";
        } else {
            return "*";
        }
    }

    /**
     * 获取文件大小
     *
     * @param file_size 文件字节数
     * @return 文件大小，优化后的文件大小显示
     */
    public static String getFileSize(long file_size) {
        double size = file_size;
        DecimalFormat df = new DecimalFormat("######0.00");
        if (file_size / 1024 / 1024 / 1024 > 0) {

            return df.format(size / 1024 / 1024 / 1024) + "G";
        } else if (file_size / 1024 / 1024 > 0) {
            return df.format(size / 1024 / 1024) + "M";
        } else if (file_size / 1024 > 0) {
            return df.format(size / 1024) + "K";
        } else {
            return df.format(size) + "B";
        }
    }

    /**
     * 判断文件名是否合法
     *
     * @param name 文件名
     * @return 文件名是否合法
     */
    public static boolean isLegal_FileName(String name) {
        // \/:*?"<>|
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '\\' || c == '/' || c == ':' || c == '*' || c == '?' || c == '<' || c == '>' || c == '|') {
                return false;
            }
        }
        return true;
    }
}
