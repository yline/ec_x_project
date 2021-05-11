package com.flight.canvas

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SparseArray

object BitmapManager {
    private val sourceMap = SparseArray<Bitmap>()

    fun newBitmap(resources: Resources, sourceId: Int): Bitmap {
        val oldBitmap = sourceMap[sourceId]
        if (null != oldBitmap) return oldBitmap

        val newBitmap = BitmapFactory.decodeResource(resources, sourceId)
        sourceMap.put(sourceId, oldBitmap)
        return newBitmap
    }

    fun clear() {
        sourceMap.clear()
    }
}