package com.example.logtrail_sdk.Utilities

import com.example.logtrail_sdk.Interfaces.LogApi
import com.example.logtrail_sdk.Monitor.LogTrailMonitor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        
        // Add network monitoring interceptor if available
        LogTrailMonitor.getNetworkInterceptor()?.let { interceptor ->
            builder.addInterceptor(interceptor)
        }
        
        builder.build()
    }

    val instance: LogApi by lazy {
        Retrofit.Builder()
            .baseUrl(LogApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LogApi::class.java)
    }
}