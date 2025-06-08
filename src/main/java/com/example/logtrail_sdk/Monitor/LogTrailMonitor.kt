package com.example.logtrail_sdk.Monitor

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * LogTrailMonitor - System-level monitoring for the LogTrail SDK
 * 
 * Automatically captures and logs:
 * - Uncaught exceptions
 * - Activity lifecycle events  
 * - Network failures and slow requests
 * 
 * This class is initialized automatically when LogTrail.init() is called.
 */
object LogTrailMonitor {
    
    private val TAG = "LogTrailMonitor"
    private var isInitialized = false
    
    private var uncaughtExceptionMonitor: UncaughtExceptionMonitor? = null
    private var activityLifecycleMonitor: ActivityLifecycleMonitor? = null
    private var networkInterceptor: LogTrailNetworkInterceptor? = null
    
    /**
     * Initialize all monitoring components
     * @param context Application or Activity context
     */
    fun initialize(context: Context) {
        if (isInitialized) {
            Log.d(TAG, "LogTrailMonitor already initialized")
            return
        }
        
        try {
            val application = context.applicationContext as Application
            
            // Initialize uncaught exception monitoring
            initializeUncaughtExceptionMonitoring()
            
            // Initialize activity lifecycle monitoring
            initializeActivityLifecycleMonitoring(application)
            
            // Initialize network monitoring (will be used by RetrofitClient)
            initializeNetworkMonitoring()
            
            isInitialized = true
            Log.d(TAG, "✅ LogTrailMonitor initialized successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize LogTrailMonitor: ${e.message}", e)
        }
    }
    
    /**
     * Initialize uncaught exception monitoring
     */
    private fun initializeUncaughtExceptionMonitoring() {
        try {
            uncaughtExceptionMonitor = UncaughtExceptionMonitor()
            uncaughtExceptionMonitor?.initialize()
            Log.d(TAG, "✅ Uncaught exception monitoring enabled")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize uncaught exception monitoring: ${e.message}")
        }
    }
    
    /**
     * Initialize activity lifecycle monitoring
     */
    private fun initializeActivityLifecycleMonitoring(application: Application) {
        try {
            activityLifecycleMonitor = ActivityLifecycleMonitor()
            application.registerActivityLifecycleCallbacks(activityLifecycleMonitor)
            Log.d(TAG, "✅ Activity lifecycle monitoring enabled")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize activity lifecycle monitoring: ${e.message}")
        }
    }
    
    /**
     * Initialize network monitoring
     */
    private fun initializeNetworkMonitoring() {
        try {
            networkInterceptor = LogTrailNetworkInterceptor()
            Log.d(TAG, "✅ Network monitoring interceptor created")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize network monitoring: ${e.message}")
        }
    }
    
    /**
     * Get the network interceptor for use in RetrofitClient
     */
    fun getNetworkInterceptor(): LogTrailNetworkInterceptor? {
        return networkInterceptor
    }
    
    /**
     * Check if monitor is initialized
     */
    fun isInitialized(): Boolean = isInitialized
    
    /**
     * Cleanup monitoring resources (for testing or shutdown)
     */
    fun cleanup() {
        try {
            // Reset uncaught exception handler if we set one
            uncaughtExceptionMonitor?.cleanup()
            
            // Activity lifecycle callbacks are automatically cleaned up when app closes
            activityLifecycleMonitor = null
            networkInterceptor = null
            
            isInitialized = false
            Log.d(TAG, "LogTrailMonitor cleaned up")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup: ${e.message}")
        }
    }
} 