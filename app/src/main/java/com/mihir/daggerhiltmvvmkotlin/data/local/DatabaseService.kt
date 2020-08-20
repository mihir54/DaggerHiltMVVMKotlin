package com.mihir.daggerhiltmvvmkotlin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mihir.daggerhiltmvvmkotlin.data.local.dao.UserDao
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import javax.inject.Singleton


@Database(
    entities = [
        UserEntity::class
    ],
    exportSchema = false,
    version = 9
)
abstract class DatabaseService : RoomDatabase() {
    abstract fun userDao() : UserDao
}