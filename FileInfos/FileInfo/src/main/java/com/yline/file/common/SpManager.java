package com.yline.file.common;

import com.yline.file.IApplication;
import com.yline.utils.SPUtil;

/**
 * SP管理类
 *
 * @author yline 2018/1/26 -- 17:30
 * @version 1.0.0
 */
public class SpManager {
    private static class Key {
        private static final String FILE_NAME = "MainSpKey";

        private static final String KEY_IS_FILE_INFO_LOADING = "isFileInfoLoading"; // 是否正在加载
        private static final String KEY_LAST_LOAD_TIME = "LastScanTime"; // 上次扫面完成时间

        private static void put(String key, Object value) {
            SPUtil.put(IApplication.getApplication(), key, value, Key.FILE_NAME);
        }

        private static Object get(String key, Object defaultValue) {
            return SPUtil.get(IApplication.getApplication(), key, defaultValue, Key.FILE_NAME);
        }
    }

    public static void setIsFileInfoLoading(boolean isLoading) {
        Key.put(Key.KEY_IS_FILE_INFO_LOADING, isLoading);
    }

    public static boolean isFileInfoLoading() {
        return (boolean) Key.get(Key.KEY_IS_FILE_INFO_LOADING, false);
    }

    public static void setLastLoadTime(long loadTime) {
        Key.put(Key.KEY_LAST_LOAD_TIME, loadTime);
    }

    public static boolean isStartLoad() {
        long lastLoadTime = (long) Key.get(Key.KEY_LAST_LOAD_TIME, 0L);
        return (System.currentTimeMillis() > lastLoadTime + 86400 * 1000 * 5);
    }
}
