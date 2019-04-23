package com.riatech.falcon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        FalconFileCache::class
    ],
    version = 1
)
abstract class FalconDB : RoomDatabase() {
    abstract fun falconFileCacheDao(): FalconFileCacheDao

    companion object {
        var INSTANCE: FalconDB? = null

        private fun getFalconDBInstance(context: Context): FalconDB? {
            if (INSTANCE == null) {
                synchronized(FalconDB::class) {
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext, FalconDB::class.java, "falconDB")
                            .build()
                }
            }
            return INSTANCE
        }

        fun cache(context: Context, falconFileCache: FalconFileCache) {
            GlobalScope.launch {
                getFalconDBInstance(context)?.falconFileCacheDao()?.insert(falconFileCache)
            }
        }

        fun getFileFromCache(context: Context, url: String?): FalconFileCache? = getFalconDBInstance(context)?.falconFileCacheDao()?.getFileByURL(url)

    }
}