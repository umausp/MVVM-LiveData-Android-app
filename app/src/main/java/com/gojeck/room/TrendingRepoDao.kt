package com.gojeck.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gojeck.feature.model.TrendingRepositoriesModelItem

@Dao
interface TrendingRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trendingRepositoriesModel: List<TrendingRepositoriesModelItem>)

    @Query("SELECT * from trendingRepositoriesEntity")
    fun getTrendingRepositories(): LiveData<List<TrendingRepositoriesModelItem>>

    @Query("DELETE FROM trendingRepositoriesEntity")
    suspend fun delete()
}
