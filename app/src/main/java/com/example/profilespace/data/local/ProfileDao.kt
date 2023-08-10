package com.example.profilespace.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.profilespace.data.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow


/**
 * Created by Omar Elashry on 8/9/2023.
 */
@Dao
interface ProfileDao {

    // taking advantage of Kotlin asynchronous flow
    @Query("select * from profile_table")
    fun getProfiles(): Flow<List<ProfileEntity>>

    @Query("select * from profile_table where id = :id")
    suspend fun getProfile(id: Int): ProfileEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileEntity: ProfileEntity): Long

    @Delete
    suspend fun deleteProfile(profileEntity: ProfileEntity):Int
}