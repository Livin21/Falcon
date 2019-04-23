package com.riatech.falcon.db

import androidx.room.*

@Dao
interface FalconFileCacheDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(file: FalconFileCache)

    @Query("SELECT * FROM FalconFileCache")
    fun getFiles(): List<FalconFileCache>

    @Query("SELECT * FROM FalconFileCache WHERE url == :url")
    fun getFileByURL(url: String?): FalconFileCache?

}