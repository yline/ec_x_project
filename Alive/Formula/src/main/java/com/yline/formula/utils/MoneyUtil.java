package com.yline.formula.utils;

/**
 * 钱为单位的工具类
 *
 * @author yline 2019-11-17 -- 17:27
 */
public class MoneyUtil {
    /**
     * 元 转 分
     *
     * @param yuan 元
     * @return 分
     */
    public static int yuan2fen(double yuan) {
        double fen = yuan * 100;
        return (int) Math.round(fen);
    }

    /**
     * 分 转 分
     *
     * @param fen 分
     * @return 分
     */
    public static int fen2fen(double fen) {
        return (int) Math.round(fen);
    }
}
