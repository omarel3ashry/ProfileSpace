package com.example.profilespace.domain.model

import com.example.profilespace.data.local.entities.ProfileEntity

/**
 * Created by Omar Elashry on 8/9/2023.
 */
data class Profile(
    var id: Int,
    var name: String,
    var jobTitle: String,
    var gender: String,
    var age: Int,
    var level:Int,
    var country:String
)
// mapper to entity model
fun Profile.toProfileEntity():ProfileEntity = ProfileEntity(
    id = id,
    name = name,
    jobTitle = jobTitle,
    gender = gender,
    age = age,
    level = level,
    country = country
)