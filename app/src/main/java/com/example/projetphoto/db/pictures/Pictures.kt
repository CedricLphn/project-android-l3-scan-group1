package com.example.projetphoto.db.pictures

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pictures(
    @ColumnInfo val title : String,
    @ColumnInfo val link : String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
