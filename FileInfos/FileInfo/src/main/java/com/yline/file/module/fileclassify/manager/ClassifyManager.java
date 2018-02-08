package com.yline.file.module.fileclassify.manager;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.yline.utils.LogUtil;

/**
 * 分类中，工具方法
 *
 * @author yline 2018/2/8 -- 11:28
 * @version 1.0.0
 */
public class ClassifyManager {
    public static final long ERROR_DURATION = -1;

    /**
     * 获取，视频缩略图
     * 15个，耗时 3631  --> 242ms / 个
     *
     * @param videoFilePath 视频路径
     * @return 缩略图，长宽中最大数，缩放到小于  512
     */
    public static Bitmap createVideoMini(String videoFilePath) {
        long startTime = System.currentTimeMillis();
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoFilePath, MediaStore.Video.Thumbnails.MINI_KIND);
        LogUtil.v("diffTime = " + (System.currentTimeMillis() - startTime));
        return bitmap;
    }

    /**
     * 获取，视频缩略图
     * 15个，耗时 2999  --> 200ms / 个
     *
     * @param videoFilePath 视频路径
     * @return 缩略图，长宽中最大数，缩放到小于  96
     */
    public static Bitmap createVideoMicro(String videoFilePath) {
        long startTime = System.currentTimeMillis();
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoFilePath, MediaStore.Video.Thumbnails.MICRO_KIND);
        LogUtil.v("diffTime = " + (System.currentTimeMillis() - startTime));
        return bitmap;
    }

    /**
     * 获取，视频播放时长
     * 耗时在 个位数; 20ms左右
     *
     * @return 视频时长，单位 ms
     */
    public static long getVideoDuration(String videoFilePath) {
        MediaMetadataRetriever retriever = null;

        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoFilePath);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long result = Long.parseLong(duration);

            // 手动校验一遍，默认采用 3000ms
            if (result < Integer.MIN_VALUE) {
                result = 3_000;
            }
            return result;
        } catch (NumberFormatException ex) {
            LogUtil.e("getVideoDuration", ex);
            return ERROR_DURATION;
        } catch (IllegalArgumentException ex) {
            LogUtil.e("getVideoDuration", ex);
            return ERROR_DURATION;
        } finally {
            if (null != retriever) {
                retriever.release();
            }
        }
    }
}
