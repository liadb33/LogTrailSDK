package com.example.logtrail_sdk.Logger

import android.util.Log
import com.example.logtrail_sdk.Data.LogItem
import com.example.logtrail_sdk.Utilities.RetrofitClient
import com.example.logtrail_sdk.Utilities.LogTrail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * LogTrailLogger - Custom logging wrapper for LogTrail SDK
 * 
 * Usage:
 * LogTrailLogger.d("MainActivity", "User tapped login")
 * LogTrailLogger.e("LoginActivity", "Login failed due to timeout")
 * LogTrailLogger.i("UserProfile", "Profile data loaded successfully")
 * 
 * Note: Make sure to call LogTrail.init(context, userId) before using this logger
 */
object LogTrailLogger {
    
    private val TAG = "LogTrailLogger"
    
    /**
     * Log a DEBUG message
     * @param tag Source tag (e.g., "MainActivity", "UserService")
     * @param message Log message
     */
    fun d(tag: String, message: String) {
        // Log to Logcat
        Log.d(tag, message)
        
        // Send to backend
        sendLogToBackend("debug", tag, message)
    }
    
    /**
     * Log an ERROR message
     * @param tag Source tag (e.g., "MainActivity", "UserService")
     * @param message Log message
     */
    fun e(tag: String, message: String) {
        // Log to Logcat
        Log.e(tag, message)
        
        // Send to backend
        sendLogToBackend("error", tag, message)
    }
    
    /**
     * Log an INFO message
     * @param tag Source tag (e.g., "MainActivity", "UserService")
     * @param message Log message
     */
    fun i(tag: String, message: String) {
        // Log to Logcat
        Log.i(tag, message)
        
        // Send to backend
        sendLogToBackend("info", tag, message)
    }
    
    /**
     * Log a WARNING message
     * @param tag Source tag (e.g., "MainActivity", "UserService")
     * @param message Log message
     */
    fun w(tag: String, message: String) {
        // Log to Logcat
        Log.w(tag, message)
        
        // Send to backend
        sendLogToBackend("warning", tag, message)
    }
    
    /**
     * Log a VERBOSE message
     * @param tag Source tag (e.g., "MainActivity", "UserService")
     * @param message Log message
     */
    fun v(tag: String, message: String) {
        // Log to Logcat
        Log.v(tag, message)
        
        // Send to backend
        sendLogToBackend("verbose", tag, message)
    }
    
    /**
     * Internal method to send log to backend
     */
    private fun sendLogToBackend(level: String, tag: String, message: String) {
        try {
            // Check if SDK is initialized
            if (!LogTrail.isInitialized()) {
                Log.w(TAG, "LogTrail SDK not initialized. Call LogTrail.init() first.")
                return
            }
            
            val userId = LogTrail.getUserId()
            if (userId == null) {
                Log.w(TAG, "User ID not available. Cannot send log to backend.")
                return
            }
            
            // Create LogItem
            val logItem = LogItem(
                userId = userId,
                level = level,
                tag = tag,
                message = message,
                timestamp = getCurrentTimestampLocal(),
                threadId = Thread.currentThread().id.toInt(),
                processId = android.os.Process.myPid(),
                packageName = LogTrail.getPackageName()
            )
            
            // Send to backend using Retrofit
            RetrofitClient.instance.sendLog(logItem).enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    if (response.isSuccessful) {
                        Log.v(TAG, "Log sent to backend successfully: $level - $tag - $message")
                    } else {
                        Log.w(TAG, "Failed to send log to backend: ${response.code()}")
                    }
                }
                
                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Log.w(TAG, "Network error sending log to backend: ${t.message}")
                }
            })
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending log to backend: ${e.message}")
        }
    }
    
    /**
     * Get current timestamp in ISO 8601 UTC format
     */
    private fun getCurrentTimestampLocal(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()  // local timezone
        return sdf.format(Date())
    }
    
    /**
     * Convenience method to log exceptions
     * @param tag Source tag
     * @param message Log message
     * @param throwable Exception to log
     */
    fun e(tag: String, message: String, throwable: Throwable) {
        val fullMessage = "$message: ${throwable.message}"
        Log.e(tag, fullMessage, throwable)
        sendLogToBackend("error", tag, fullMessage)
    }
    
    /**
     * Check if the logger is ready to use
     */
    fun isReady(): Boolean {
        return LogTrail.isInitialized()
    }
} 