package com.example.homework

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UsernameDB : RoomDatabase() {
    abstract val dao: UserDao
}
