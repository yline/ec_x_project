package com.yline.file.module.fileclassify.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.yline.utils.LogUtil;

import java.util.Locale;

/**
 * 分类中，工具方法
 *
 * @author yline 2018/2/8 -- 11:28
 * @version 1.0.0
 */
public class ClassifyManager {
    public static final long ERROR_DURATION = 0;

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

    public static String getVideoDurationFormat(String videoFilePath) {
        long millisTime = getFileDuration(videoFilePath);
        return String.format(Locale.CHINA, "%02d:%02d:%02d", millisTime / 3_600_000 % 60,
                millisTime / 60_000 % 60, millisTime / 1000 % 60);
    }

    public static String getAudioDurationFormat(String audioFilePath) {
        long millisTime = getFileDuration(audioFilePath);
        return String.format(Locale.CHINA, "%02d:%02d", millisTime / 60_000, millisTime / 1000 % 60);
    }

    /**
     * 获取，视频播放时长
     * 耗时在 个位数; 20ms左右
     *
     * @return 视频时长，单位 ms
     */
    private static long getFileDuration(String videoFilePath) {
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


    public static Bitmap getAudioBitmap(String audioFilePath) {
        MediaMetadataRetriever retriever = null;

        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(audioFilePath);

            byte[] audioBytes = retriever.getEmbeddedPicture();
            if (null != audioBytes) {
                return BitmapFactory.decodeByteArray(audioBytes, 0, audioBytes.length);
            } else {
                return null;
            }
        } finally {
            if (null != retriever) {
                retriever.release();
            }
        }
    }

    public static void getAudioInfo(String audioFilePath) {
        long startTime = System.currentTimeMillis();
        MediaMetadataRetriever retriever = null;

        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(audioFilePath);

            Bitmap bitmap = retriever.getFrameAtTime();
            byte[] bytes = retriever.getEmbeddedPicture();

            String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
            String composer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
            String date = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
            String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
//            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String tracks = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
            String mimetype = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            String albumartist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            String number = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER);
            String compilation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION);
            String audio = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
            String video = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            String location = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
            String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            String framerate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE);

            LogUtil.v("filePath = " + audioFilePath + ", diffTime = " + (System.currentTimeMillis() - startTime));
            LogUtil.v(String.format("bitmap = %s, bytes = %s, album = %s, artist = %s, author = %s, composer = %s, date = %s, genre = %s" +
                            ", title = %s, year = %s, duration = %s, tracks = %s, mimetype = %s, albumartist = %s, number = %s, compilation = %s, audio = %s" +
                            ", video = %s, width = %s, height = %s, bitrate = %s, location = %s, rotation = %s, framerate = %s",
                    (bitmap == null ? "null" : "bitmap"), (null == bytes ? "null" : "bytes"), album, artist, author, composer, date, genre, "ex", year,
                    duration, tracks, mimetype, albumartist, number, compilation, audio, video, width, height, bitrate, location, rotation, framerate));
        } catch (NumberFormatException ex) {
            LogUtil.e("getAudioInfo", ex);
        } catch (IllegalArgumentException ex) {
            LogUtil.e("getAudioInfo", ex);
        } finally {
            if (null != retriever) {
                retriever.release();
            }
        }
    }
}
