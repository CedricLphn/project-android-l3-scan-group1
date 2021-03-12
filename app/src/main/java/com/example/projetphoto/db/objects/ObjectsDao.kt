package com.example.projetphoto.db.objects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ObjectsDao {
    @Query("SELECT * FROM Objects ORDER BY id DESC")
    fun getAll() : List<Objects>

    @Query("SELECT * FROM Objects WHERE photo_id = :photo_id")
    fun getObjects(photo_id : Int) : List<Objects>

    @Query("SELECT COUNT(*) FROM Objects WHERE photo_id = :id")
    fun countObjectsPhoto(id : Int) : Int

    @Insert
    fun insert(obj : Objects)

}