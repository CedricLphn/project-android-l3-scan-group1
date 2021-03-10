package com.example.projetphoto.pictureList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projetphoto.R

private val pictures = listOf(
    Picture(
        R.drawable.out,
        "2021-06-24",
        "Premier test",
        4
    ),
    Picture(
        R.drawable.out2,
        "2020-08-12",
        "Second test",
        2
    ),
    Picture(
        R.drawable.out3,
        "2019-02-05",
        "Troisième test",
        3
    ),
    Picture(
        R.drawable.out4,
        "2019-04-18",
        "Quatrième test",
        1
    ),
    Picture(
        R.drawable.out5,
        "2018-07-28",
        "Cinquième test",
        6
    ),
    Picture(
        R.drawable.out,
        "2021-06-24",
        "Premier test",
        4
    ),
    Picture(
        R.drawable.out2,
        "2020-08-12",
        "Second test",
        2
    ),
    Picture(
        R.drawable.out3,
        "2019-02-05",
        "Troisième test",
        3
    ),
    Picture(
        R.drawable.out4,
        "2019-04-18",
        "Quatrième test",
        1
    ),
    Picture(
        R.drawable.out5,
        "2018-07-28",
        "Cinquième test",
        6
    )
)

class PictureListViewModel : ViewModel() {
    private val picturesLiveData = MutableLiveData<List<Picture>>()
    fun getPicturesLiveData(): LiveData<List<Picture>> = picturesLiveData

    fun loadPictures() {

        picturesLiveData.value = pictures
    }
}