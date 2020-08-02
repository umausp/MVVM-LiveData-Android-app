package com.gojeck.feature.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "title") var title: String?
)
