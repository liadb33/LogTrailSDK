package com.example.logtrail_sdk.Utilities

import android.content.Context
import android.util.Log
import com.example.logtrail_sdk.Data.LogTrailConfig
import com.example.logtrail_sdk.Monitor.LogTrailMonitor
import java.lang.ref.WeakReference

/**
 * Main LogTrail SDK initialization class
 * Call LogTrail.init(context, config) before using LogTrailLogger
 */
object LogTrail {
    
    private var applicationContext: Context? = null
    private var config: LogTrailConfig? = null
    private var isInitialized = false
    private var packageName: String? = null
    
    /**
     * Initialize the LogTrail SDK with configuration
     * @param context Application or Activity context (will be converted to Application context)
     * @param config LogTrailConfig containing all SDK settings
     */
    fun init(context: Context, config: LogTrailConfig) {
        // Use application context to avoid memory leaks
        this.applicationContext = context.applicationContext
        this.config = config
        this.packageName = context.packageName
        this.isInitialized = true
        
        // Setup debug logging based on config
        if (config.enableDebugLogs) {
            Log.d("LogTrail", "SDK initialized with debug logging enabled")
            Log.d("LogTrail", "Base URL: ${config.baseUrl}")
            Log.d("LogTrail", "User ID: ${config.userId}")
            Log.d("LogTrail", "Monitoring enabled: ${config.enableMonitoring}")
        } else {
            Log.d("LogTrail", "SDK initialized for user: ${config.userId}")
        }
        
        // Initialize monitoring components if enabled
        if (config.enableMonitoring) {
            LogTrailMonitor.initialize(context, config)
        }
        
        // Initialize Retrofit with the new base URL
        RetrofitClient.initialize(config)
    }
    
    /**
     * Initialize the LogTrail SDK (deprecated method for backward compatibility)
     * @param context Application or Activity context
     * @param userId The user ID for logging
     * @deprecated Use init(context, config) instead
     */
    @Deprecated(
        message = "Use init(context, config) instead",
        replaceWith = ReplaceWith("init(context, LogTrailConfig.create(\"your_base_url\", userId))")
    )
    fun init(context: Context, userId: String) {
        Log.w("LogTrail", "Using deprecated init method. Please migrate to init(context, LogTrailConfig)")
        // For backward compatibility, use a default base URL (this should be updated by developers)
        val defaultConfig = LogTrailConfig.create("http://localhost:5000/", userId)
        init(context, defaultConfig)
    }
    
    /**
     * Get the stored application context (safe from memory leaks)
     */
    internal fun getApplicationContext(): Context? = applicationContext
    
    /**
     * Get the stored configuration
     */
    internal fun getConfig(): LogTrailConfig? = config
    
    /**
     * Get the stored user ID
     */
    internal fun getUserId(): String? = config?.userId
    
    /**
     * Check if SDK is initialized
     */
    internal fun isInitialized(): Boolean = isInitialized
    
    /**
     * Get package name from stored context
     */
    internal fun getPackageName(): String? = packageName
    
    /**
     * Clean up resources (optional, for testing or special cases)
     */
    fun cleanup() {
        // Cleanup monitoring first
        LogTrailMonitor.cleanup()
        
        // Cleanup Retrofit client
        RetrofitClient.cleanup()
        
        applicationContext = null
        config = null
        packageName = null
        isInitialized = false
        Log.d("LogTrail", "SDK cleaned up")
    }
} 