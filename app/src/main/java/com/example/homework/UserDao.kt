package com.example.homework

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    @Upsert
    suspend fun upsert(user: User)

    @Query("SELECT * FROM User ORDER BY uid DESC LIMIT 1") // Get the first user
    suspend fun getUser(): User?
}
