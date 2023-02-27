package com.example.weatherapp.network

import com.example.weatherapp.models.Root
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    //"onecall?lat=29.9604&lon=31.2885&appid=4a059725f93489b95183bbcb8c6829b9&units=metric&lang=en"

    @GET("onecall")
    suspend fun getCurrentTempData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Response<Root>


}