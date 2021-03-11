package com.example.projetphoto.itemDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.example.projetphoto.R
import com.example.projetphoto.databinding.ActivityItemDetailsBinding
import com.example.projetphoto.db.AppDatabase
import com.example.projetphoto.db.objects.Objects
import com.example.projetphoto.db.pictures.Pictures

class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val idPhoto = intent.getStringExtra("idPhoto")
//        val idPhoto = intent.getIntExtra("idPhoto", -1)
        val idPhone = 1 //À changer par le getIntExtra quand le lien sera fait

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "test1"
        ).allowMainThreadQueries().build()

//        val test = Pictures(3, "test", "lol")
//        val objtest = Objects(3, "Velo", 95.2, 3)
//        val objtest2 = Objects(2, "Voiture", 87.6, 1)
//        db.picturesDao().insert(test)
//        db.objectDao().insert(objtest)
//        db.objectDao().insert(objtest2)

        val listObj: List<Objects> = db.objectDao().getObjects(idPhone)
        val picture: Pictures = db.picturesDao().getPicture(idPhone)

        binding.titlePhotoTextView.text = picture.title
        for (i in listObj.indices) {
            binding.nameObjectTextView.text =
                "${binding.nameObjectTextView.text} \n ${listObj[i].name}"
        }
        for (i in listObj.indices) {
            binding.valueObjectTextView.text =
                "${binding.valueObjectTextView.text} \n ${listObj[i].score* 100}%"
        }
    }
}