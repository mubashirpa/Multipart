package com.example.multipart.data

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Streaming

interface FileApi {
    @Multipart
    @POST("upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Call<ResponseBody>

    @Streaming
    @GET("download")
    suspend fun downloadImage(): ResponseBody

    companion object {
        val instance by lazy {
            Retrofit
                .Builder()
                .baseUrl("http://192.168.220.33:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FileApi::class.java)
        }
    }
}
