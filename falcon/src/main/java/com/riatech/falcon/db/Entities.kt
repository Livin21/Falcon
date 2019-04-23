package com.riatech.falcon.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream
import java.io.File

@Entity
data class FalconFileCache(
    @PrimaryKey val url: String,
    var timestamp: Long,
    var expiry: Long? = null
) {
    fun loadFile(context: Context): Bitmap? = BitmapFactory.decodeFile(File(context.filesDir, timestamp.toString()).absolutePath)
    fun createCache(context: Context, bitmap: Bitmap) {
        context.openFileOutput(timestamp.toString(), Context.MODE_PRIVATE).use {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            it.write(stream.toByteArray())
            stream.close()
            FalconDB.cache(context, this)
        }
    }
}
