package com.app.fitlife.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date


@Entity
data class User(
    val name:String,
    @PrimaryKey
    val email:String,
    val password: String,
    val birth: String,
    val weight: String,
    val height: String,
    val gender: String,
    val goal: String,
    val weeklyExercise: String,
    val exerciseType :String
):Serializable
