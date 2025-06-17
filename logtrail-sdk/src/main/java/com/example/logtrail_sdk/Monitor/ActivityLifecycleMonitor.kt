package com.example.logtrail_sdk.Monitor

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.example.logtrail_sdk.Logger.LogTrailLogger

/**
 * ActivityLifecycleMonitor - Monitors activity lifecycle events
 * 
 * Uses Application.ActivityLifecycleCallbacks to track when activities
 * are created, started, resumed, paused, stopped, and destroyed.
 */
class ActivityLifecycleMonitor : Application.ActivityLifecycleCallbacks {
    
    private val TAG = "ActivityLifecycleMonitor"
    
    // Track activity stack for analytics
    private val activityStack = mutableListOf<String>()
    
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val activityName = activity.javaClass.simpleName
        val message = "onCreate - $activityName"
        
        LogTrailLogger.i("Lifecycle", message)
        
        // Track if this is a fresh start or restoration
        val isRestored = savedInstanceState != null
        if (isRestored) {
            LogTrailLogger.d("LifecycleDetail", "Activity restored from saved state - $activityName")
        }
        
        activityStack.add(activityName)
        logActivityStackInfo()
    }
    
    override fun onActivityStarted(activity: Activity) {
        val activityName = activity.javaClass.simpleName
        val message = "onStart - $activityName"
        
        LogTrailLogger.i("Lifecycle", message)
    }
    
    override fun onActivityResumed(activity: Activity) {
        val activityName = activity.javaClass.simpleName
        val message = "onResume - $activityName"
        
        LogTrailLogger.i("Lifecycle", message)
        
        // Log that this activity is now the foreground activity
        LogTrailLogger.d("LifecycleDetail", "Activity in foreground - $activityName")
    }
    
    override fun onActivityPaused(activity: Activity) {
        val activityName = activity.javaClass.simpleName
        val message = "onPause - $activityName"
        
        LogTrailLogger.i("Lifecycle", message)
    }
    
    override fun onActivityStopped(activity: Activity) {
        val activityName = activity.javaClass.simpleName
        val message = "onStop - $activityName"
        
        LogTrailLogger.i("Lifecycle", message)
        
        // Log that activity is no longer visible
        LogTrailLogger.d("LifecycleDetail", "Activity no longer visible - $activityName")
    }
    
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val activityName = activity.javaClass.simpleName
        val message = "onSaveInstanceState - $activityName"
        
        LogTrailLogger.d("Lifecycle", message)
    }
    
    override fun onActivityDestroyed(activity: Activity) {
        val activityName = activity.javaClass.simpleName
        val message = "onDestroy - $activityName"
        
        LogTrailLogger.i("Lifecycle", message)
        
        // Remove from activity stack
        activityStack.remove(activityName)
        logActivityStackInfo()
        
        // Check if activity was finished explicitly
        if (activity.isFinishing) {
            LogTrailLogger.d("LifecycleDetail", "Activity finished explicitly - $activityName")
        } else {
            LogTrailLogger.w("LifecycleDetail", "Activity destroyed without finish - $activityName")
        }
    }
    
    /**
     * Log information about the current activity stack
     */
    private fun logActivityStackInfo() {
        try {
            val stackSize = activityStack.size
            val currentActivity = activityStack.lastOrNull() ?: "None"
            
            LogTrailLogger.d("ActivityStack", "Stack size: $stackSize, Current: $currentActivity")
            
            // Log full stack if it's getting large (potential memory leak indicator)
            if (stackSize > 5) {
                val stackInfo = activityStack.joinToString(" -> ")
                LogTrailLogger.w("ActivityStack", "Large activity stack detected: $stackInfo")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error logging activity stack info: ${e.message}")
        }
    }
    
    /**
     * Get current activity stack (for debugging)
     */
    fun getCurrentActivityStack(): List<String> {
        return activityStack.toList()
    }
    
    /**
     * Get the current foreground activity name
     */
    fun getCurrentActivity(): String? {
        return activityStack.lastOrNull()
    }
} 