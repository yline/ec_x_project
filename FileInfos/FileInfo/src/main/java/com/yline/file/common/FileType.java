package com.yline.file.common;

import android.support.annotation.NonNull;

import com.yline.file.R;

import java.io.Serializable;

/**
 * 现阶段，支持的文件类型
 *
 * @author yline 2018/2/6 -- 17:32
 * @version 1.0.0
 */
public enum FileType implements Serializable {
    VIDEO(10, "视频", R.drawable.icon_type_video, "%.3gp", "%.avi", "%.mov", "%.mp4", "%.mpeg", "%.mpg", "%.mpg4"), // 视频
    AUDIO(20, "音频", R.drawable.icon_type_audio, "%.m4a", "%.mp3", "%.mpga", "%.ogg", "%.rmvb", "%.wav", "%.wma", "%.wmv"), // 音频
    IMAGE(30, "图片", R.drawable.icon_type_image, "%.jpg", "%.gif", "%.png", "%.jpeg", "%.bmp", "%.webp"), // 图片
    APK(40, "安装包", R.drawable.icon_type_app, "%.apk"), // App安装包
    WORD(50, "Word", R.drawable.icon_type_word, "%.doc", "%.docx"), // word
    EXCEL(60, "Excel", R.drawable.icon_type_excel, "%.xls", "%.xlsx", "%.xlt"), // excel
    PPT(70, "PPT", R.drawable.icon_type_ppt, "%.ppt", "%.pps"), // PPT
    PDF(80, "PDF", R.drawable.icon_type_pdf, "%.pdf"), // PDF
    TEXT(90, "文本", R.drawable.icon_type_txt, "%.c", "%.conf", "%.cpp", "%.h", "%.java", "%.log", "%.prop", "%.c", "%.c", "%.c"), // txt
    HTML(100, "Html", R.drawable.icon_type_html, "%.c", "%.conf"), // html
    UNKNOW(0, "其它", R.drawable.icon_type_other);

    private int mFid; // 编号
    private String mStr; // 名称
    private int mResId; // 资源文件
    private String[] mDbSelection; // 数据库查询条件

    /**
     * 文件类型
     *
     * @param fid   编号
     * @param dbStr 名称
     */
    FileType(int fid, String dbStr, int resId, String... selection) {
        this.mFid = fid;
        this.mStr = dbStr;
        this.mResId = resId;
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
    public int getResId() {
        return mResId;
    }

    @NonNull
    public String[] getDbSelection() {
        return mDbSelection;
    }
}