package com.example.profilespace.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.profilespace.domain.model.Profile

/**
 * Created by Omar Elashry on 8/9/2023.
 */
@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var jobTitle: String,
    var gender: String,
    var age: Int,
    var level: Int,
    var country: String
)
// mapper to domain model
fun ProfileEntity.toProfile(): Profile = Profile(
    id = id,
    name = name,
    jobTitle = jobTitle,
    gender = gender,
    age = age,
    level = level,
    country = country
)
