package com.example.projetphoto.pictureList

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projetphoto.databinding.ActivityPictureListBinding
import com.example.projetphoto.takepictures.TakePicturesActivity

val lauchTakePicture = 1
private const val TAG = "MyActivity"

class PictureListActivity : AppCompatActivity() {

    private val model : PictureListViewModel by viewModels()

    private lateinit var binding: ActivityPictureListBinding
    private lateinit var adapter: PictureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model.getState().observe(this, Observer { state -> updateUi(state!!) })

        adapter = PictureAdapter(listOf())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        model.loadPictures()

        binding.floatingActionButton.setOnClickListener {

           val intent = Intent(this, TakePicturesActivity::class.java)
            startActivityForResult(intent, lauchTakePicture);

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data!!)
        if (requestCode == lauchTakePicture) {
            if (resultCode == RESULT_OK) {
                val result = data.getStringExtra("result")
                Log.i(TAG, "onActivityResult: $result")
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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