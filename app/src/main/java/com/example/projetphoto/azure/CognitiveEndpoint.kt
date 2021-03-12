package com.example.projetphoto.azure

import com.example.projetphoto.utils.ConfigurationUtils
import com.example.projetphoto.utils.ObjectRoot
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CognitiveEndpoint {
    @Multipart
    @Headers(
        "Ocp-Apim-Subscription-Key: ${ConfigurationUtils.AZ_SECRET_KEY}")
    @POST("vision/v3.0/detect")
    fun SendImage(@Part("file\"; filename=\"pp.png\" ") file : RequestBody) : Call<ObjectRoot>
}