package com.example.homework

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int = 1,
    @ColumnInfo(name = "username") val userName: String,
    @ColumnInfo(name = "image") val profilePic: String
)
