package com.yline.file.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yline.file.IApplication;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.utils.LogUtil;

import java.io.File;
import java.io.Serializable;

/**
 * 1）跳转，系统，app
 * 2）打开文件
 *
 * @author yline 2018/2/1 -- 10:19
 * @version 1.0.0
 */
public class IntentUtils {
    public static FileType getFileType(String path) {
        if (TextUtils.isEmpty(path)) {
            return FileType.UNKNOW;
        }

        if (isFileTypeAudio(path)) {
            return FileType.AUDIO;
        } else if (isFileTypeVideo(path)) {
            return FileType.VIDEO;
        } else if (isFileTypeImage(path)) {
            return FileType.IMAGE;
        } else if (isFileTypeApk(path)) {
            return FileType.APK;
        } else if (isFileTypePPT(path)) {
            return FileType.PPT;
        } else if (isFileTypeExcel(path)) {
            return FileType.EXCEL;
        } else if (isFileTypeWord(path)) {
            return FileType.WORD;
        } else if (isFileTypePdf(path)) {
            return FileType.PDF;
        } else if (isFileTypeText(path)) {
            return FileType.TEXT;
        } else if (isFileTypeHtml(path)) {
            return FileType.HTML;
        } else {
            return FileType.UNKNOW;
        }
    }

    public static boolean isFileTypeAudio(String path) {
        return (path.endsWith(".m4a") || path.endsWith(".mp3") || path.endsWith(".mpga") || path.endsWith(".ogg") || path.endsWith(".rmvb") || path.endsWith(".wav") || path.endsWith(".wma") || path.endsWith(".wmv"));
    }

    public static boolean isFileTypeVideo(@NonNull String path) {
        return (path.endsWith(".3gp") || path.endsWith(".avi") || path.endsWith(".mov") || path.endsWith(".mp4") || path.endsWith(".mpeg") || path.endsWith(".mpg") || path.endsWith(".mpg4"));
    }

    public static boolean isFileTypeImage(@NonNull String path) {
        return (path.endsWith(".jpg") || path.endsWith(".gif") || path.endsWith(".png") || path.endsWith(".jpeg") || path.endsWith(".bmp") || path.endsWith(".webp"));
    }

    public static boolean isFileTypeApk(@NonNull String path) {
        return (path.endsWith(".apk"));
    }

    public static boolean isFileTypePPT(@NonNull String path) {
        return (path.endsWith(".ppt") || path.endsWith(".pps"));
    }

    public static boolean isFileTypeExcel(@NonNull String path) {
        return (path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith(".xlt"));
    }

    public static boolean isFileTypeWord(@NonNull String path) {
        return (path.endsWith(".doc") || path.endsWith(".docx"));
    }

    public static boolean isFileTypePdf(@NonNull String path) {
        return (path.endsWith(".pdf"));
    }

    public static boolean isFileTypeText(@NonNull String path) {
        return (path.endsWith(".c") || path.endsWith(".conf") || path.endsWith(".cpp") || path.endsWith(".h") || path.endsWith(".java")
                || path.endsWith(".log") || path.endsWith(".prop") || path.endsWith(".rc") || path.endsWith(".sh") || path.endsWith(".txt") || path.endsWith(".xml"));
    }

    public static boolean isFileTypeHtml(@NonNull String path) {
        return (path.endsWith(".htm") || path.endsWith(".html"));
    }

    public static void openFileAll(Context context, FileInfoModel fileModel) {
        Intent intent = IntentUtils.getIntentAll(fileModel.getAbsolutePath());
        if (null != intent) {
            if (null != intent.resolveActivity(context.getPackageManager())) {
                context.startActivity(intent);
            } else {
                LogUtil.v("file: " + fileModel.getAbsolutePath() + ", cannot resolve");
                IApplication.toast("文件无法打开");
            }
        }
    }

    /**
     * 打开某一个文件
     */
    public static Intent getIntentAll(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        String mimeType = "*/*";
        for (String[] table : MIME_MapTable) {
            if (filePath.endsWith(table[0])) {
                mimeType = table[1];
                break;
            }
        }
        return getIntentAll(mimeType, Uri.fromFile(file));
    }

    private static Intent getIntentAll(@NonNull String mimeType, @NonNull Uri uri) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);

        return intent;
    }

    /**
     * 文件类型
     */
    public enum FileType implements Serializable {
        VIDEO(2, "视频", "%.3gp", "%.avi", "%.mov", "%.mp4", "%.mpeg", "%.mpg", "%.mpg4"), // 视频
        AUDIO(1, "音频", "%.m4a", "%.mp3", "%.mpga", "%.ogg", "%.rmvb", "%.wav", "%.wma", "%.wmv"), // 音频
        IMAGE(3, "图片", "%.jpg", "%.gif", "%.png", "%.jpeg", "%.bmp", "%.webp"), // 图片
        APK(4, "安装包", "%.apk"), // App安装包
        WORD(7, "Word", "%.doc", "%.docx"), // word
        EXCEL(6, "Excel", "%.xls", "%.xlsx", "%.xlt"), // excel
        PPT(5, "PPT", "%.ppt", "%.pps"), // PPT
        PDF(8, "PDF", "%.pdf"), // PDF
        TEXT(9, "文本", "%.c", "%.conf", "%.cpp", "%.h", "%.java", "%.log", "%.prop", "%.c", "%.c", "%.c"), // txt
        HTML(10, "Html", "%.c", "%.conf"), // html
        UNKNOW(0, "其它");

        private int mFid; // 编号
        private String mStr; // 名称
        private String[] mDbSelection; // 数据库查询条件

        /**
         * 文件类型
         *
         * @param fid   编号
         * @param dbStr 名称
         */
        FileType(int fid, String dbStr, String... selection) {
            this.mFid = fid;
            this.mStr = dbStr;
            this.mDbSelection = (null == selection ? new String[]{} : selection);
        }

        public int getFid() {
            return mFid;
        }

        @NonNull
        public String getStr() {
            return mStr;
        }

        @NonNull
        public String[] getDbSelection() {
            return mDbSelection;
        }
    }

    // 建立一个MIME类型与文件后缀名的匹配表
    private final static String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".webp", "image/webp"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.ms-excel"},
            {".xlt", "application/vnd.ms-excel"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}
