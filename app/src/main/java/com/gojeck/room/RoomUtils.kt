package com.gojeck.room

import androidx.room.Room
import com.gojeck.utils.ctx
import com.gojeck.utils.isTesting

inline fun <reified T : BaseRoomDatabase> createDatabase(name: String = T::class.simpleName!!): T {
    if (isTesting) {
        return Room.inMemoryDatabaseBuilder(
            ctx,
            T::class.java
        ).allowMainThreadQueries().build()
    }

    return Room.databaseBuilder(ctx, T::class.java, name).build()
}
