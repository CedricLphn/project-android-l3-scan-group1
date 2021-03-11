package com.example.projetphoto.db.pictures

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PicturesDao {
    @Query("SELECT * FROM pictures")
    fun getAll() : List<Pictures>

    @Insert
    fun insert(picture : Pictures)
}