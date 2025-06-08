package com.example.logtrail_sdk.Utilities

import android.content.Context
import com.example.logtrail_sdk.Monitor.LogTrailMonitor
import java.lang.ref.WeakReference

/**
 * Main LogTrail SDK initialization class
 * Call LogTrail.init(context, userId) before using LogTrailLogger
 */
object LogTrail {
    
    private var applicationContext: Context? = null
    private var userId: String? = null
    private var isInitialized = false
    private var packageName: String? = null
    
    /**
     * Initialize the LogTrail SDK
     * @param context Application or Activity context (will be converted to Application context)
     * @param userId The user ID for logging
     */
    fun init(context: Context, userId: String) {
        // Use application context to avoid memory leaks
        this.applicationContext = context.applicationContext
        this.userId = userId
        this.packageName = context.packageName
        this.isInitialized = true
        
        android.util.Log.d("LogTrail", "SDK initialized for user: $userId")
        
        // Initialize monitoring components
        LogTrailMonitor.initialize(context)
    }
    
    /**
     * Get the stored application context (safe from memory leaks)
     */
    internal fun getApplicationContext(): Context? = applicationContext
    
    /**
     * Get the stored user ID
     */
    internal fun getUserId(): String? = userId
    
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
        
        applicationContext = null
        userId = null
        packageName = null
        isInitialized = false
        android.util.Log.d("LogTrail", "SDK cleaned up")
    }
} 