package com.example.profilespace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.profilespace.data.local.entities.ProfileEntity

/**
 * Created by Omar Elashry on 8/9/2023.
 */
@Database(entities = [ProfileEntity::class], version = 1, exportSchema = false)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        const val DB_NAME = "profile_data_db"
    }
}