package com.gojeck.room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: ArrayList<T>)

    @Query("select * from tasks")
    fun getTrendingRepositories(): LiveData<T>

    @Delete
    suspend fun delete(obj: T)
}