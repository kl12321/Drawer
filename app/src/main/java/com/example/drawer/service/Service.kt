package com.example.drawer.service

import com.example.drawer.entity.Results
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL="https://pixabay.com/"
private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
             .build()

interface PixabayRequest{
    @GET("api/?key=25780115-6005698adaff4b0f507e8e7a4&lang=zh")
    suspend fun getPixabayList(@Query("q") keyWord:String):Results
}
object Service{
    val retrofitService:PixabayRequest by lazy {
        retrofit.create(PixabayRequest::class.java)
    }
}
