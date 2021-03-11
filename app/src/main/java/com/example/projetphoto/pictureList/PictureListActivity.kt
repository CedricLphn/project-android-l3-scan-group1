package com.example.projetphoto.pictureList

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.projetphoto.R
import com.example.projetphoto.azure.CognitiveEndpoint
import com.example.projetphoto.azure.CognitiveServiceBuilder
import com.example.projetphoto.databinding.ActivityPictureListBinding
import com.example.projetphoto.db.AppDatabase
import com.example.projetphoto.db.objects.Objects
import com.example.projetphoto.db.pictures.Pictures
import com.example.projetphoto.takepictures.TakePicturesActivity
import com.example.projetphoto.utils.ObjectRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


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


        bdd = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "test1"
        ).allowMainThreadQueries().build()

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
        super.onActivityResult(requestCode, resultCode, data)
        val contentResolver = applicationContext.contentResolver

        if (requestCode == lauchTakePicture) {
            if (resultCode == RESULT_OK) {
                val result = data?.getStringExtra("result")
                Log.i(TAG, "onActivityResult: $result")


                CoroutineScope(Dispatchers.IO).launch {
                    val file = File(result)
                    val api = sendToApi(file)

                        var picture = Pictures("test", result!!)
                        bdd.picturesDao().insert(picture)

                        for (item in api!!.objects) {
                            Log.i(TAG, "onActivityResult: Adding object: ${item.name}")
                            bdd.objectDao().insert(Objects(item.name,
                                item.confidence,
                                bdd.picturesDao().getLastId()))
                        }


                }

            }

            //Log.i(TAG, "onActivityResult: $filedata")
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    suspend fun sendToApi(file: File): ObjectRoot? {
        val fbody = RequestBody.create("image/*".toMediaTypeOrNull(),
            file);

        val api = CognitiveServiceBuilder.buildService(CognitiveEndpoint::class.java)

        var response = api.SendImage(fbody).execute();
        var callback = response.body();

        return callback
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