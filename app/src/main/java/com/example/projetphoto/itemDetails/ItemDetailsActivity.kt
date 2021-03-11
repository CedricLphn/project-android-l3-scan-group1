package com.example.projetphoto.itemDetails

import android.net.Uri
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

        val idPhoto = intent.getIntExtra("idPhoto", -1)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "test1"
        ).allowMainThreadQueries().build()


        val listObj: List<Objects> = db.objectDao().getObjects(idPhoto)
        val picture: Pictures = db.picturesDao().getPicture(idPhoto)
        binding.pictureDetailImageView.setImageURI(Uri.parse(picture.link))
        binding.titlePhotoTextView.text = picture.title
        for (i in listObj.indices) {
            binding.nameObjectTextView.text =
                "${binding.nameObjectTextView.text} \n ${listObj[i].name}"
        }
        for (i in listObj.indices) {
            binding.valueObjectTextView.text =
                "${binding.valueObjectTextView.text} \n${"%.2f".format(listObj[i].score* 100)}%"
        }
    }
}