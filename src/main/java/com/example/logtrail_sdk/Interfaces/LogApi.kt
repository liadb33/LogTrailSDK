package com.example.logtrail_sdk.Interfaces

import android.util.Log
import com.example.logtrail_sdk.Data.LogItem
import com.example.logtrail_sdk.Utilities.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LogApi {

    companion object{
        const val BASE_URL : String = "http://IP_ADDRESS:5000/"
    }

    @POST("logs/")
    fun sendLog(@Body log: LogItem) : Call<Map<String, String>>

    @GET("logs/")
    fun getLogs(
        @Query("userId") userId: String? = null,
        @Query("level") level: String? = null,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null,
        @Query("tag") tag: String? = null,
        @Query("packageName") packageName: String? = null
    ) : Call<List<LogItem>>

    @GET("logs/all")
    fun getAllLogs() : Call<List<LogItem>>
}