package com.example.projetphoto.pictureList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        model.getState().observe(this, Observer { state -> updateUi(state!!)})

        adapter = PictureAdapter(listOf())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        model.loadPictures()
    }

    private fun updateUi(state: PictureListViewModelState) {
        when (state) {
            PictureListViewModelState.Loading -> {
                Toast.makeText(
                    this@PictureListActivity,
                    "Chargement des images !",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is PictureListViewModelState.Failure -> {
                Toast.makeText(
                    this@PictureListActivity,
                    "Liste d'images vide / Erreur lors du chargement des images !",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is PictureListViewModelState.Success -> {
                Toast.makeText(
                    this@PictureListActivity,
                    "Chargement des images réussi !",
                    Toast.LENGTH_SHORT
                ).show()
                adapter.updateDataSet(state.pictures)
            }
        }
    }
}