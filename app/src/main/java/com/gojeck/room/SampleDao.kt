package com.gojeck.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gojeck.feature.model.Task

@Dao
interface SampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: ArrayList<Task>)
}
