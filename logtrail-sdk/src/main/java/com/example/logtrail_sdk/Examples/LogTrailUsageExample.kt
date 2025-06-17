package com.example.logtrail_sdk.Examples

import android.app.Application
import android.content.Context
import com.example.logtrail_sdk.Data.LogTrailConfig
import com.example.logtrail_sdk.Utilities.LogTrail

/**
 * Example usage of LogTrail SDK with configuration
 * This file demonstrates the correct way to initialize and use the LogTrail SDK
 */
class LogTrailUsageExample {

    /**
     * Example 1: Basic initialization with required parameters
     */
    fun basicInitialization(context: Context) {
        val config = LogTrailConfig.create(
            baseUrl = "https://your-logtrail-api.com/",
            userId = "user123"
        )
        
        LogTrail.init(context, config)
    }

    /**
     * Example 2: Initialization with monitoring disabled
     */
    fun initializationWithoutMonitoring(context: Context) {
        val config = LogTrailConfig.createWithoutMonitoring(
            baseUrl = "https://your-logtrail-api.com/",
            userId = "user123"
        )
        
        LogTrail.init(context, config)
    }

    /**
     * Example 3: Initialization with debug logging enabled
     */
    fun initializationWithDebugLogs(context: Context) {
        val config = LogTrailConfig.createWithDebugLogs(
            baseUrl = "https://your-logtrail-api.com/",
            userId = "user123"
        )
        
        LogTrail.init(context, config)
    }

    /**
     * Example 4: Full configuration with custom settings
     */
    fun fullConfiguration(context: Context) {
        val config = LogTrailConfig(
            baseUrl = "https://your-logtrail-api.com/",
            userId = "user123",
            enableMonitoring = true,
            enableDebugLogs = false,
            connectionTimeoutSeconds = 45,
            readTimeoutSeconds = 60
        )
        
        LogTrail.init(context, config)
    }

    /**
     * Example 5: Configuration with custom timeouts
     */
    fun configurationWithCustomTimeouts(context: Context) {
        val config = LogTrailConfig.createWithCustomTimeouts(
            baseUrl = "https://your-logtrail-api.com/",
            userId = "user123",
            connectionTimeoutSeconds = 30,
            readTimeoutSeconds = 45
        )
        
        LogTrail.init(context, config)
    }

    /**
     * Example 6: Initialization in Application class (recommended)
     */
    class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            
            // Initialize LogTrail SDK as early as possible
            val config = LogTrailConfig.create(
                baseUrl = "https://your-logtrail-api.com/",
                userId = getCurrentUserId() // Your method to get user ID
            )
            
            LogTrail.init(this, config)
        }
        
        private fun getCurrentUserId(): String {
            // Your logic to determine the current user ID
            // This could come from SharedPreferences, user session, etc.
            return "user123"
        }
    }

    /**
     * Example 7: Different configurations for different environments
     */
    fun environmentBasedConfiguration(context: Context, isDebug: Boolean = false) {
        val config = when {
            isDebug -> LogTrailConfig(
                baseUrl = "http://localhost:5000/",
                userId = "debug_user",
                enableMonitoring = true,
                enableDebugLogs = true
            )
            else -> LogTrailConfig.create(
                baseUrl = "https://api.logtrail.com/",
                userId = getCurrentUserId()
            )
        }
        
        LogTrail.init(context, config)
    }
    
    private fun getCurrentUserId(): String {
        // Your implementation to get current user ID
        return "user123"
    }
}

/**
 * Quick reference for developers:
 * 
 * 1. Basic usage:
 *    val config = LogTrailConfig.create("https://your-api.com/", "user123")
 *    LogTrail.init(context, config)
 * 
 * 2. With monitoring disabled:
 *    val config = LogTrailConfig.createWithoutMonitoring("https://your-api.com/", "user123")
 *    LogTrail.init(context, config)
 * 
 * 3. With debug logs:
 *    val config = LogTrailConfig.createWithDebugLogs("https://your-api.com/", "user123")
 *    LogTrail.init(context, config)
 * 
 * 4. Full customization:
 *    val config = LogTrailConfig(
 *        baseUrl = "https://your-api.com/",
 *        userId = "user123",
 *        enableMonitoring = true,
 *        enableDebugLogs = false,
 *        connectionTimeoutSeconds = 30,
 *        readTimeoutSeconds = 30
 *    )
 *    LogTrail.init(context, config)
 */ 