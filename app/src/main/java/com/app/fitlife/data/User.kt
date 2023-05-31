package com.app.fitlife.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date


@Entity
data class User(
    var name:String,
    @PrimaryKey
    var email:String,
    var password: String,
    var birth: String,
    var weight: String,
    var height: String,
    var gender: String,
    var goal: String,
    var weeklyExercise: String,
    var exerciseType :String
):Serializable
