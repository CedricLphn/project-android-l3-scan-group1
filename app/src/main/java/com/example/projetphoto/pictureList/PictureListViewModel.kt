package com.example.projetphoto.pictureList

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetphoto.azure.CognitiveEndpoint
import com.example.projetphoto.azure.CognitiveServiceBuilder
import com.example.projetphoto.db.AppDatabase
import com.example.projetphoto.db.db_init
import com.example.projetphoto.db.objects.Objects
import com.example.projetphoto.db.pictures.Pictures
import com.example.projetphoto.utils.ObjectRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private val TAG = "PictureListViewModel"

sealed class PictureListViewModelState(
    open val errorMessage: String = ""
) {
    object Loading : PictureListViewModelState()
    data class Success(val pictures: MutableList<Pictures>) : PictureListViewModelState()
    data class Failure(override val errorMessage: String) :
        PictureListViewModelState(errorMessage = errorMessage)
}

class PictureListViewModel : ViewModel() {
    private val state = MutableLiveData<PictureListViewModelState>()

    fun getState(): LiveData<PictureListViewModelState> = state

    fun loadPictures(bdd : AppDatabase) {
        var pictures = bdd.picturesDao().getAll()
        state.postValue(PictureListViewModelState.Loading)
        if (pictures.isNullOrEmpty()) {
            state.postValue(PictureListViewModelState.Failure("Aucune image dans la liste"))
        } else {
            state.postValue(PictureListViewModelState.Success(pictures))
        }

    }

    fun insert(rootFile: String, title: String, date: String, bdd: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File(rootFile)
            val api = sendToApi(file)

            var picture = Pictures(title, date, rootFile, api!!.objects.size)
            bdd.picturesDao().insert(picture)

            for (item in api.objects) {
                Log.i(TAG, "onActivityResult: Adding object: ${item.name}")
                bdd.objectDao().insert(Objects(item.name,
                    item.confidence,
                    bdd.picturesDao().getLastId()))
            }

            state.postValue(PictureListViewModelState.Success(bdd.picturesDao().getAll()))

        }

    }

    suspend fun sendToApi(file: File): ObjectRoot? {
        val fbody = file.asRequestBody("image/*".toMediaTypeOrNull())

        val api = CognitiveServiceBuilder.buildService(CognitiveEndpoint::class.java)

        var response = api.SendImage(fbody).execute()
        var callback = response.body()

        return callback


    }
}