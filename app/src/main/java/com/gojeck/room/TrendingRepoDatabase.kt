package com.gojeck.room

import androidx.room.Database
import com.gojeck.feature.model.TrendingRepositoriesModelItem

@Database(
    entities = [TrendingRepositoriesModelItem::class],
    version = 1,
    exportSchema = false
)
abstract class TrendingRepoDatabase : BaseRoomDatabase() {
    abstract fun getTrendingRepoDao(): TrendingRepoDao
}
