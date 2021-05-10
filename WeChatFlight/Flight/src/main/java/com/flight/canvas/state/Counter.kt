package com.flight.canvas.state

import java.util.*

/**
 * 注意int 和 float 不能相同的tag.
 * 不同的计算,不要使用相同的tag,不然会有计算错误.
 * 使用案例:
 * isTrue = counter.caculate(0.15f, 2.3f);
 * if (isTrue) {
 * System.out.println("i = " + i + ",tag1" + isTrue);
 * }
 *
 * @author yline
 * @date 2016-4-6
 */
class Counter {
    private val map: HashMap<String, Any>
    var judge // 给外界用的
            = false

    fun caculate(durate: Float, friz: Float): Boolean {
        return caculate(DEFAULT_TAG, durate, friz)
    }

    /**
     * 倒计时,只要一直被执行, 那么每过 friz/durate 次 就会返回一次true
     * @param tag    标签
     * @param durate    每次刷新间隔
     * @param friz        通知间隔
     * @return
     */
    fun caculate(tag: String, durate: Float, friz: Float): Boolean {
        if (!map.containsKey(tag)) {
            map[tag] = 0.0f
        } else {
            if ((map[tag] as Float?)!! > friz) {
                map[tag] = 0.0f
                return true
            } else {
                map[tag] = (map[tag] as Float?)!! + durate
            }
        }
        return false
    }

    /**
     * 倒计时,清零,默认的倒计时器
     */
    fun clearCaculate() {
        if (map.containsKey(DEFAULT_TAG)) {
            map[DEFAULT_TAG] = 0.0f
        }
    }

    /**
     * 倒计时,清零
     * @param tag    标记
     */
    fun clearCaculate(tag: String) {
        if (map.containsKey(tag)) {
            map[tag] = 0.0f
        }
    }

    companion object {
        private const val DEFAULT_TAG = "DEFAULT"
    }

    init {
        map = HashMap()
    }
}