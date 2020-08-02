package com.gojeck.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gojeck.feature.model.TrendingRepositoriesModelItem
import com.gojeck.utils.ctx

@Database(
    entities = [TrendingRepositoriesModelItem::class],
    version = 1,
    exportSchema = false
)
abstract class TrendingRepoDatabase : RoomDatabase() {
    abstract fun getTrendingRepoDao(): TrendingRepoDao

    companion object {
        private const val DB_NAME = "TrendingRepoDatabase"
        private var instance: TrendingRepoDatabase? = null

        fun getTrendingDBInstance(): TrendingRepoDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(ctx, TrendingRepoDatabase::class.java, DB_NAME).build()
            }
            return instance!!
        }
    }
}
