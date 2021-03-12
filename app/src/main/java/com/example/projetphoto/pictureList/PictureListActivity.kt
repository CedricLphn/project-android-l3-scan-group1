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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetphoto.databinding.ActivityPictureListBinding
import com.example.projetphoto.db.AppDatabase
import com.example.projetphoto.db.db_init
import com.example.projetphoto.gesture.SwipeToDeleteCallback
import com.example.projetphoto.takepictures.TakePicturesActivity


val lauchTakePicture = 1
private const val TAG = "PictureListActivity"

class PictureListActivity : AppCompatActivity() {

    private val model: PictureListViewModel by viewModels()

    private lateinit var binding: ActivityPictureListBinding
    private lateinit var adapter: PictureAdapter

    private lateinit var bdd: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureListBinding.inflate(layoutInflater)


        bdd = db_init(applicationContext)

        setContentView(binding.root)

        model.getState().observe(this, Observer { state -> updateUi(state!!) })

        adapter = PictureAdapter(arrayListOf())
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as PictureAdapter
                val pictureRemove = adapter.removeAt(viewHolder.adapterPosition)
                bdd.picturesDao().delete(pictureRemove)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        model.loadPictures(applicationContext)

        binding.floatingActionButton.setOnClickListener {

            val intent = Intent(this, TakePicturesActivity::class.java)
            startActivityForResult(intent, lauchTakePicture)

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == lauchTakePicture) {
            if (resultCode == RESULT_OK) {
                val rootFile = data?.getStringExtra("rootFile")
                val title = data?.getStringExtra("title")
                val date = data?.getStringExtra("date")
                Log.i(TAG, "data rootFile: $rootFile")
                Log.i(TAG, "data title: $title")
                Log.i(TAG, "data date: $date")
                model.insert(rootFile!!, title!!, date!!, applicationContext)
            }

            //Log.i(TAG, "onActivityResult: $filedata")
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
                    "Loading images !",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is PictureListViewModelState.Failure -> {
                Toast.makeText(
                    this@PictureListActivity,
                    "Empty image list !",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is PictureListViewModelState.Success -> {
                Toast.makeText(
                    this@PictureListActivity,
                    "Loading of images successful !",
                    Toast.LENGTH_SHORT
                ).show()
                adapter.updateDataSet(state.pictures)
            }
        }
    }
}