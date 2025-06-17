package com.example.logtrail_sdk.Logger

import android.content.Context
import android.util.Log
import com.example.logtrail_sdk.Utilities.LogTrail

/**
 * Test class for LogTrailLogger functionality
 * This class demonstrates how to use the LogTrailLogger and tests all its methods
 */
class LogTrailLoggerTest(private val context: Context) {
    
    private val TAG = "LogTrailLoggerTest"
    
    /**
     * Initialize and run all LogTrailLogger tests
     */
    fun runAllLoggerTests() {
        Log.d(TAG, "🚀 Starting LogTrailLogger Tests...")
        
        // Step 1: Initialize the SDK
        testSDKInitialization()
        
        // Step 2: Test all logging levels
        testAllLoggingLevels()
        
        // Step 3: Test exception logging
        testExceptionLogging()
        
        // Step 4: Test logger state
        testLoggerState()
        
        Log.d(TAG, "🏁 LogTrailLogger Tests completed!")
    }
    
    /**
     * Test SDK initialization
     */
    private fun testSDKInitialization() {
        Log.d(TAG, "📋 Testing SDK initialization...")
        
        // Test before initialization
        val readyBefore = LogTrailLogger.isReady()
        Log.d(TAG, "Logger ready before init: $readyBefore")
        
        // Initialize SDK
        LogTrail.init(context, "test_user_logger_123")
        
        // Test after initialization
        val readyAfter = LogTrailLogger.isReady()
        Log.d(TAG, "Logger ready after init: $readyAfter")
        
        Log.d(TAG, "✅ SDK initialization test completed")
    }
    
    /**
     * Test all logging levels
     */
    private fun testAllLoggingLevels() {
        Log.d(TAG, "📋 Testing all logging levels...")
        
        // Test DEBUG logging
        LogTrailLogger.d("TestActivity", "This is a debug message from LogTrailLogger")
        
        // Test INFO logging
        LogTrailLogger.i("TestService", "This is an info message from LogTrailLogger")
        
        // Test WARNING logging
        LogTrailLogger.w("TestFragment", "This is a warning message from LogTrailLogger")
        
        // Test ERROR logging
        LogTrailLogger.e("TestUtility", "This is an error message from LogTrailLogger")
        
        // Test VERBOSE logging
        LogTrailLogger.v("TestReceiver", "This is a verbose message from LogTrailLogger")
        
        Log.d(TAG, "✅ All logging levels test completed")
    }
    
    /**
     * Test exception logging
     */
    private fun testExceptionLogging() {
        Log.d(TAG, "📋 Testing exception logging...")
        
        try {
            // Simulate an exception
            throw RuntimeException("Test exception for LogTrailLogger")
        } catch (e: Exception) {
            // Log the exception using the convenience method
            LogTrailLogger.e("TestExceptionHandler", "Caught an exception during testing", e)
        }
        
        Log.d(TAG, "✅ Exception logging test completed")
    }
    
    /**
     * Test logger state and edge cases
     */
    private fun testLoggerState() {
        Log.d(TAG, "📋 Testing logger state...")
        
        // Test if logger is ready
        if (LogTrailLogger.isReady()) {
            Log.d(TAG, "✅ Logger is ready and initialized")
        } else {
            Log.e(TAG, "❌ Logger is not ready")
        }
        
        // Test rapid logging (performance test)
        val startTime = System.currentTimeMillis()
        for (i in 1..10) {
            LogTrailLogger.d("PerformanceTest", "Rapid log message #$i")
        }
        val endTime = System.currentTimeMillis()
        Log.d(TAG, "⚡ Rapid logging test completed in ${endTime - startTime}ms")
        
        Log.d(TAG, "✅ Logger state test completed")
    }
    
    /**
     * Test logging from different thread contexts
     */
    fun testMultiThreadLogging() {
        Log.d(TAG, "📋 Testing multi-thread logging...")
        
        // Log from main thread
        LogTrailLogger.i("MainThread", "Log from main thread")
        
        // Log from background thread
        Thread {
            LogTrailLogger.i("BackgroundThread", "Log from background thread")
            LogTrailLogger.d("BackgroundThread", "Thread ID: ${Thread.currentThread().id}")
        }.start()
        
        // Log from another background thread
        Thread {
            LogTrailLogger.w("WorkerThread", "Log from worker thread")
            LogTrailLogger.d("WorkerThread", "Thread ID: ${Thread.currentThread().id}")
        }.start()
        
        Log.d(TAG, "✅ Multi-thread logging test initiated")
    }
    
    /**
     * Demonstrate typical usage patterns
     */
    fun demonstrateUsagePatterns() {
        Log.d(TAG, "📋 Demonstrating typical usage patterns...")
        
        // Simulate user actions
        LogTrailLogger.i("MainActivity", "User opened the app")
        LogTrailLogger.d("MainActivity", "Loading user preferences")
        
        // Simulate navigation
        LogTrailLogger.d("Navigator", "Navigating to user profile")
        LogTrailLogger.i("ProfileActivity", "Profile screen loaded")
        
        // Simulate network operations
        LogTrailLogger.d("NetworkManager", "Starting API request")
        LogTrailLogger.i("NetworkManager", "API request completed successfully")
        
        // Simulate error scenarios
        LogTrailLogger.w("ImageLoader", "Image cache miss, loading from network")
        LogTrailLogger.e("PaymentService", "Payment processing failed - insufficient funds")
        
        // Simulate debugging information
        LogTrailLogger.v("DatabaseHelper", "Query executed: SELECT * FROM users WHERE id = 123")
        LogTrailLogger.v("CacheManager", "Cache hit ratio: 85%")
        
        Log.d(TAG, "✅ Usage patterns demonstration completed")
    }
} 