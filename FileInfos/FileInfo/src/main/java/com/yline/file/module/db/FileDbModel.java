package com.yline.file.module.db;

/**
 * 文件，数据库，储存数据
 *
 * @author yline 2018/1/29 -- 11:28
 * @version 1.0.0
 */
public class FileDbModel {
    /**
     * 如果是文件，结尾不带 '/'
     * 如果是文件夹，结尾带 '/'
     */
    private String absolutePath;
    private boolean isDir; // 是否是文件夹，用于直接统计个数
    private byte[] data;

    public FileDbModel(String absolutePath, boolean isDir, byte[] data) {
        this.absolutePath = absolutePath;
        this.isDir = isDir;
        this.data = data;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
