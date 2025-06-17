package com.example.logtrail_sdk.Logger

import android.content.Context
import android.util.Log

/**
 * Example usage of LogTrailSDKTest
 * You can call these methods from your Activity, Fragment, or any Android component
 */
class LogTrailSDKTestExample {

    companion object {
        private const val TAG = "LogTrailSDKTestExample"

        /**
         * Initialize and run all tests
         * Call this method from your MainActivity onCreate() or any other method
         */
        fun runCompleteAPITest(context: Context) {
            Log.d(TAG, "🔧 Initializing LogTrail SDK Test Suite...")
            
            val testSuite = LogTrailSDKTest()
            
            // Run all tests
            testSuite.runAllTests()
        }

        /**
         * Run individual tests
         */
        fun runIndividualTests(context: Context) {
            Log.d(TAG, "🔧 Running individual LogTrail SDK tests...")
            
            val testSuite = LogTrailSDKTest()
            
            // Test individual endpoints
            testSuite.testSendLog()
            
            // Wait and test others
            android.os.Handler().postDelayed({
                testSuite.testGetAllLogs()
            }, 2000)
            
            android.os.Handler().postDelayed({
                testSuite.testGetFilteredLogsByUser("test_user_123")
            }, 4000)
            
            android.os.Handler().postDelayed({
                testSuite.testGetFilteredLogsByLevel("error")
            }, 6000)
        }

        /**
         * Test only the basic functionality (send and get)
         */
        fun runBasicTests(context: Context) {
            Log.d(TAG, "🔧 Running basic LogTrail SDK tests...")
            
            val testSuite = LogTrailSDKTest()
            
            // Test sending a log
            testSuite.testSendLog()
            
            // Wait 2 seconds then test getting logs
            android.os.Handler().postDelayed({
                testSuite.testGetAllLogs()
            }, 2000)
        }
    }
} 