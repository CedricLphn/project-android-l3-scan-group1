package com.example.projetphoto.azure

import com.example.projetphoto.utils.ConfigurationUtils
import com.example.projetphoto.utils.ObjectRoot
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.ByteString
import okio.ByteString.Companion.toByteString
import retrofit2.Call
import retrofit2.http.*
import java.io.ByteArrayInputStream

public interface CognitiveEndpoint {
    @Multipart
    @Headers(
        "Ocp-Apim-Subscription-Key: ${ConfigurationUtils.AZ_SECRET_KEY}")
    @POST("vision/v3.0/detect")
    fun SendImage(@Part("file\"; filename=\"pp.png\" ") file : RequestBody) : Call<ObjectRoot>
}

data class JsonTest(val content : ByteArray)