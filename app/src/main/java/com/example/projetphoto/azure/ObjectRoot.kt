package com.example.projetphoto.utils

import com.google.gson.annotations.SerializedName

data class ObjectRoot(
    val objects : List<ObjectModel>,
    val requestId : String
)

data class ObjectModel (
    @SerializedName("object") val name : String,
    val confidence : Double
)
