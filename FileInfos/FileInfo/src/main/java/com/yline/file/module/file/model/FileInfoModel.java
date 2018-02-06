package com.yline.file.module.file.model;

import com.yline.file.common.IntentUtils;

import java.io.Serializable;

/**
 * 文件，管理器
 *
 * @author yline 2018/1/29 -- 17:02
 * @version 1.0.0
 */
public class FileInfoModel implements Serializable {
    /**
     * 如果是文件，结尾不带 '/'
     * 如果是文件夹，结尾带 '/'
     */
    private String absolutePath; // 结尾不带 '/'
    private boolean isDirectory;

    private String fileName;
    private int childDirCount;
    private int childFileCount;
    private long fileSize;

    private int fileType;

    /**
     * 新建文件的信息
     *
     * @param fileName         文件名
     * @param fileAbsolutePath 文件绝对路径
     * @param fileSize         文件大小
     */
    public FileInfoModel(String fileName, String fileAbsolutePath, long fileSize, int fileType) {
        this.isDirectory = false;
        this.fileName = fileName;
        this.absolutePath = fileAbsolutePath;
        this.childDirCount = 0;
        this.childFileCount = 0;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    /**
     * 新建 文件夹信息
     *
     * @param fileName         文件夹名
     * @param fileAbsolutePath 文件夹绝对路径
     * @param dirCount         文件夹,子目录的文件夹个数
     * @param fileCount        文件夹,子目录的文件个数
     * @param dirSize          该文件夹大小
     */
    public FileInfoModel(String fileName, String fileAbsolutePath, int dirCount, int fileCount, long dirSize) {
        this.isDirectory = true;
        this.fileName = fileName;
        this.absolutePath = fileAbsolutePath;
        this.childDirCount = dirCount;
        this.childFileCount = fileCount;
        this.fileSize = dirSize;
        this.fileType = IntentUtils.FileType.UNKNOW.getFid();
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public int getChildDirCount() {
        return childDirCount;
    }

    public void setChildDirCount(int childDirCount) {
        this.childDirCount = childDirCount;
    }

    public int getChildFileCount() {
        return childFileCount;
    }

    public void setChildFileCount(int childFileCount) {
        this.childFileCount = childFileCount;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
