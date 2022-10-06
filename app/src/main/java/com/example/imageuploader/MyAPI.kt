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
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface MyAPI {

    @Multipart
    @POST("upload")
//    @POST("Api.php?apicall=upload")

    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("test") test: RequestBody,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    @Multipart
    @PUT("/editbankadmin/{bank_admin_id}")

    fun edit(
        @Part image: MultipartBody.Part,
        @Path("bank_admin_id") bank_admin_id: String,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): MyAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
//                .baseUrl("http://10.71.104.125/ImageUploader/")
                .baseUrl("http://10.71.104.171:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(MyAPI::class.java)
        }
    }
}