package com.example.projetphoto.db.objects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ObjectsDao {
    @Query("SELECT * FROM Objects")
    fun getAll() : List<Objects>

    @Insert
    fun insert(obj : Objects)

}