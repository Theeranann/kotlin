package com.example.testuploadimg

import com.example.imageuploader.UploadResponse
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface MyAPI {

    @Multipart
    @POST("upload")
//    @POST("Api.php?apicall=upload")

    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): MyAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
//                .baseUrl("http://10.71.104.125/ImageUploader/")
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(MyAPI::class.java)
        }
    }
}