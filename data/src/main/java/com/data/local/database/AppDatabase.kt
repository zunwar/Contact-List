package com.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.data.entities.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

}