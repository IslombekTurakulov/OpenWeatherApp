package com.iuturakulov.openweatherapp.api

import com.google.gson.GsonBuilder
import com.iuturakulov.openweatherapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().create()

    val client: Retrofit
        get() {
            if (retrofit == null) {
                synchronized(Retrofit::class.java)
                {
                    if (retrofit == null) {
                        val client = OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()
                        retrofit = Retrofit.Builder()
                            .baseUrl(BuildConfig.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build()
                    }
                }
            }
            return retrofit!!
        }
}
