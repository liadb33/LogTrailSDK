package com.example.logtrail_sdk.Utilities

import android.util.Log
import com.example.logtrail_sdk.Data.LogTrailConfig
import com.example.logtrail_sdk.Interfaces.LogApi
import com.example.logtrail_sdk.Monitor.LogTrailMonitor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var logApi: LogApi? = null
    private var config: LogTrailConfig? = null
    
    /**
     * Initialize the Retrofit client with the given configuration
     * @param config LogTrailConfig containing base URL and timeout settings
     */
    fun initialize(config: LogTrailConfig) {
        this.config = config
        
        val okHttpClient = createOkHttpClient(config)
        
        val retrofit = Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        logApi = retrofit.create(LogApi::class.java)
        
        if (config.enableDebugLogs) {
            Log.d("RetrofitClient", "Initialized with base URL: ${config.baseUrl}")
        }
    }
    
    /**
     * Create OkHttpClient with configuration and monitoring interceptor
     */
    private fun createOkHttpClient(config: LogTrailConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(config.connectionTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(config.readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(config.readTimeoutSeconds, TimeUnit.SECONDS)
        
        // Add network monitoring interceptor if available and enabled
        if (config.enableMonitoring) {
            LogTrailMonitor.getNetworkInterceptor()?.let { interceptor ->
                builder.addInterceptor(interceptor)
                if (config.enableDebugLogs) {
                    Log.d("RetrofitClient", "Network monitoring interceptor added")
                }
            }
        }
        
        return builder.build()
    }

    /**
     * Get the LogApi instance
     * @throws IllegalStateException if not initialized
     */
    val instance: LogApi
        get() {
            return logApi ?: throw IllegalStateException(
                "RetrofitClient not initialized. Make sure to call LogTrail.init() first."
            )
        }
    
    /**
     * Check if RetrofitClient is initialized
     */
    fun isInitialized(): Boolean = logApi != null
    
    /**
     * Get the current configuration
     */
    internal fun getConfig(): LogTrailConfig? = config
    
    /**
     * Clean up the client (for testing or shutdown)
     */
    internal fun cleanup() {
        logApi = null
        config = null
        Log.d("RetrofitClient", "Cleaned up")
    }
}