package com.riatech.falcon

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import com.riatech.falcon.db.FalconDB
import com.riatech.falcon.db.FalconFileCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection


class Falcon internal constructor(private val activity: Activity) {

    private lateinit var url: String

    companion object{
        fun launch(activity: Activity): Falcon = Falcon(activity)
    }

    fun deployPayload(url: String?): Falcon{
        if (url.isNullOrEmpty() || !Patterns.WEB_URL.matcher(url).matches())
            throw IllegalArgumentException("Invalid URL")

        this.url = url

        return this

    }

    fun land(imageView: ImageView?){
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default){
                val cache = withContext(Dispatchers.Default) {
                    FalconDB.getFileFromCache(
                        activity,
                        url
                    )
                }
                val bitmap = if (cache != null) cache.loadFile(activity) else getBitmapFromURL(url)
                activity.runOnUiThread {
                    imageView?.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = java.net.URL(src)
            val connection = url
                .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            FalconFileCache(src, System.currentTimeMillis()).createCache(activity, bitmap)
            bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }

}