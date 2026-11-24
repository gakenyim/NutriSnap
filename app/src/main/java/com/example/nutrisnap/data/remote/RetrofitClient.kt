package com.example.nutrisnap.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val USDA_BASE_URL =
        "https://api.nal.usda.gov/fdc/v1/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val usdaApi: UsdaApi by lazy {
        Retrofit.Builder()
            .baseUrl(USDA_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsdaApi::class.java)
    }

}