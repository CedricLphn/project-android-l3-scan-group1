package com.example.projetphoto.db.pictures

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PicturesDao {
    @Query("SELECT * FROM pictures")
    fun getAll() : List<Pictures>

    @Query("SELECT id FROM pictures ORDER BY id DESC LIMIT 1")
    fun getLastId() : Int

    @Query("SELECT * FROM pictures WHERE id = :id")
    fun getPicture(id: Int) : Pictures

    @Insert
    fun insert(picture : Pictures)
}