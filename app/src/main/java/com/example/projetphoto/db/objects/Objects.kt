package com.example.projetphoto.db.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.projetphoto.db.pictures.Pictures

@Entity(foreignKeys = [ForeignKey(
    entity = Pictures::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("photo_id"),
    onDelete = CASCADE
)])

data class Objects(
    @ColumnInfo val name : String,
    @ColumnInfo val score : Double,
    @ColumnInfo val photo_id : Int
) {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
