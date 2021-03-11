package com.example.projetphoto.azure

import com.example.projetphoto.utils.ConfigurationUtils
import com.example.projetphoto.utils.ObjectRoot
import retrofit2.Call
import retrofit2.http.*

public interface CognitiveEndpoint {

    @Headers("Content-type: application/json",
        "Ocp-Apim-Subscription-Key: ${ConfigurationUtils.AZ_SECRET_KEY}")
    @POST("vision/v3.0/detect")
    fun SendImage(@Body photo: JsonTest) : Call<ObjectRoot>
}

data class JsonTest(val url : String)