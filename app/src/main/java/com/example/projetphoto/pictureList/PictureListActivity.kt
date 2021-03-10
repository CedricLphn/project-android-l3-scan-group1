package com.example.projetphoto.pictureList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetphoto.databinding.ActivityPictureListBinding

class PictureListActivity : AppCompatActivity() {

    private val model : PictureListViewModel by viewModels()

    private lateinit var binding: ActivityPictureListBinding
    private lateinit var adapter: PictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.getPicturesLiveData().observe(this, Observer { photos -> updatePhotos(photos!!) })

        adapter = PictureAdapter(listOf())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        model.loadPictures()
    }

    private fun updatePhotos(photos: List<Picture>) {
        adapter.updateDataSet(photos)
    }
}