package com.example.logtrailsdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.logtrail_sdk.Data.LogTrailConfig
import com.example.logtrail_sdk.Utilities.LogTrail
import com.example.logtrail_sdk.Logger.LogTrailLogger
import com.example.logtrail_sdk.Monitor.LogTrailMonitorTest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize LogTrail SDK with configuration
        // Option 1: Basic configuration
        val config = LogTrailConfig.create(
            baseUrl = "http://10.100.102.31:5000/", // Replace with your actual API URL
            userId = "main_user_123"
        )
        
        // Option 2: With debug logging enabled (uncomment to use)
        // val config = LogTrailConfig.createWithDebugLogs(
        //     baseUrl = "http://192.168.1.100:5000/",
        //     userId = "main_user_123"
        // )
        
        // Option 3: Full configuration with custom settings (uncomment to use)
        // val config = LogTrailConfig(
        //     baseUrl = "http://192.168.1.100:5000/",
        //     userId = "main_user_123",
        //     enableMonitoring = true,
        //     enableDebugLogs = true,
        //     connectionTimeoutSeconds = 30,
        //     readTimeoutSeconds = 30
        // )
        
        LogTrail.init(this, config)
        
        // Now use LogTrailLogger for all logging
        LogTrailLogger.i("MainActivity", "App started successfully")
        LogTrailLogger.d("MainActivity", "SDK initialized and ready to use")

        // Test the monitoring system
        testLogTrailMonitor()

        // Test basic logging functionality
//        LogTrailLogger.v("MainActivity", "This is a verbose message")
//        LogTrailLogger.d("MainActivity", "This is a debug message")
//        LogTrailLogger.i("MainActivity", "This is an info message")
//        LogTrailLogger.w("MainActivity", "This is a warning message")
//        LogTrailLogger.e("MainActivity", "This is an error message")

//        initViews()
//        findViews()
    }

    private fun testLogTrailMonitor() {
        LogTrailLogger.i("MainActivity", "Starting LogTrail Monitor tests")
        
        // Run comprehensive monitor tests
        val monitorTest = LogTrailMonitorTest(this)
        monitorTest.runAllMonitoringTests()
        monitorTest.demonstrateRealWorldScenarios()
        
        LogTrailLogger.i("MainActivity", "LogTrail Monitor testing completed")
    }

    private fun findViews() {
        LogTrailLogger.d("MainActivity", "Finding views")
    }

    private fun initViews() {
        LogTrailLogger.d("MainActivity", "Initializing views")
    }
    
    override fun onStart() {
        super.onStart()
        LogTrailLogger.d("MainActivity", "Activity started")
    }
    
    override fun onResume() {
        super.onResume()
        LogTrailLogger.d("MainActivity", "Activity resumed")
    }
    
    override fun onPause() {
        super.onPause()
        LogTrailLogger.d("MainActivity", "Activity paused")
    }
    
    override fun onStop() {
        super.onStop()
        LogTrailLogger.d("MainActivity", "Activity stopped")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LogTrailLogger.d("MainActivity", "Activity destroyed")
    }
}