package com.example.projetphoto.itemDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.projetphoto.R
import com.example.projetphoto.databinding.ActivityItemDetailsBinding

class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idPhoto = intent.getStringExtra("idPhoto")
        Log.i("TAG", "idPhoto test: $idPhoto")
        //val idPhoto = intent.getIntExtra("idPhoto", -1)
    }
}