package com.example.logtrail_sdk.Monitor

import android.util.Log
import com.example.logtrail_sdk.Logger.LogTrailLogger

/**
 * UncaughtExceptionMonitor - Captures uncaught exceptions globally
 * 
 * Uses Thread.setDefaultUncaughtExceptionHandler to catch crashes
 * and log them before the app terminates.
 */
class UncaughtExceptionMonitor {
    
    private val TAG = "UncaughtExceptionMonitor"
    private var originalHandler: Thread.UncaughtExceptionHandler? = null
    
    /**
     * Initialize the uncaught exception handler
     */
    fun initialize() {
        try {
            // Store the original handler to chain it
            originalHandler = Thread.getDefaultUncaughtExceptionHandler()
            
            // Set our custom handler
            Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
                handleUncaughtException(thread, exception)
                
                // Chain to the original handler (important for proper crash handling)
                originalHandler?.uncaughtException(thread, exception)
            }
            
            Log.d(TAG, "Uncaught exception handler installed")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to set uncaught exception handler: ${e.message}")
        }
    }
    
    /**
     * Handle uncaught exceptions
     */
    private fun handleUncaughtException(thread: Thread, exception: Throwable) {
        try {
            val threadName = thread.name
            val exceptionName = exception.javaClass.simpleName
            val exceptionMessage = exception.message ?: "No message"
            val stackTrace = getFormattedStackTrace(exception)
            
            val logMessage = buildString {
                append("App crashed in thread '$threadName'\n")
                append("Exception: $exceptionName\n")
                append("Message: $exceptionMessage\n")
                append("Stack trace:\n$stackTrace")
            }
            
            // Log to LogTrail backend
            LogTrailLogger.e("UncaughtException", logMessage)
            
            // Also log locally for immediate debugging
            Log.e(TAG, "UNCAUGHT EXCEPTION: $logMessage", exception)
            
            // Additional crash analytics can be added here
            logCrashAnalytics(threadName, exceptionName, exceptionMessage, stackTrace)
            
        } catch (e: Exception) {
            // Ensure we don't crash while handling a crash
            Log.e(TAG, "Error handling uncaught exception: ${e.message}")
        }
    }
    
    /**
     * Format stack trace for logging
     */
    private fun getFormattedStackTrace(exception: Throwable): String {
        return try {
            val stackTrace = StringBuilder()
            
            // Add the main exception
            stackTrace.append("${exception.javaClass.name}: ${exception.message}\n")
            
            // Add stack trace elements (limit to first 10 for brevity)
            exception.stackTrace.take(10).forEach { element ->
                stackTrace.append("    at $element\n")
            }
            
            // Add cause if present
            exception.cause?.let { cause ->
                stackTrace.append("Caused by: ${cause.javaClass.name}: ${cause.message}\n")
                cause.stackTrace.take(5).forEach { element ->
                    stackTrace.append("    at $element\n")
                }
            }
            
            stackTrace.toString()
        } catch (e: Exception) {
            "Failed to format stack trace: ${e.message}"
        }
    }
    
    /**
     * Log additional crash analytics
     */
    private fun logCrashAnalytics(threadName: String, exceptionName: String, message: String, stackTrace: String) {
        try {
            // Log crash metadata
            LogTrailLogger.e("CrashAnalytics", "Thread: $threadName, Exception: $exceptionName")
            
            // Determine crash category
            val crashCategory = categorizeCrash(exceptionName, stackTrace)
            LogTrailLogger.e("CrashCategory", crashCategory)
            
            // Log memory info if available
            val runtime = Runtime.getRuntime()
            val memoryInfo = buildString {
                append("Memory - Used: ${(runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024}MB, ")
                append("Free: ${runtime.freeMemory() / 1024 / 1024}MB, ")
                append("Total: ${runtime.totalMemory() / 1024 / 1024}MB, ")
                append("Max: ${runtime.maxMemory() / 1024 / 1024}MB")
            }
            LogTrailLogger.i("CrashMemory", memoryInfo)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error logging crash analytics: ${e.message}")
        }
    }
    
    /**
     * Categorize crash type for analytics
     */
    private fun categorizeCrash(exceptionName: String, stackTrace: String): String {
        return when {
            exceptionName.contains("OutOfMemory") -> "MEMORY_ERROR"
            exceptionName.contains("NullPointer") -> "NULL_POINTER"
            exceptionName.contains("IndexOutOfBounds") -> "INDEX_ERROR"
            exceptionName.contains("ClassCast") -> "CAST_ERROR"
            exceptionName.contains("Security") -> "SECURITY_ERROR"
            exceptionName.contains("Network") || stackTrace.contains("network") -> "NETWORK_ERROR"
            exceptionName.contains("SQLite") || stackTrace.contains("database") -> "DATABASE_ERROR"
            stackTrace.contains("Activity") -> "UI_ERROR"
            else -> "UNKNOWN_ERROR"
        }
    }
    
    /**
     * Cleanup the exception handler
     */
    fun cleanup() {
        try {
            originalHandler?.let {
                Thread.setDefaultUncaughtExceptionHandler(it)
                Log.d(TAG, "Uncaught exception handler restored")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up exception handler: ${e.message}")
        }
    }
} 