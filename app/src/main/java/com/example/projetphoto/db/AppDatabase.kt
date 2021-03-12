package com.example.projetphoto.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projetphoto.db.objects.Objects
import com.example.projetphoto.db.objects.ObjectsDao
import com.example.projetphoto.db.pictures.Pictures
import com.example.projetphoto.db.pictures.PicturesDao


@Database(entities = [Pictures::class, Objects::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun picturesDao(): PicturesDao
    abstract fun objectDao() : ObjectsDao
}

fun db_init(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "test1"
    ).allowMainThreadQueries().build()
}