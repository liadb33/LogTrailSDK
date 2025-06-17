package com.example.logtrail_sdk.Logger

import android.util.Log
import com.example.logtrail_sdk.Data.LogItem
import com.example.logtrail_sdk.Utilities.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LogTrailSDKTest {

    private val api = RetrofitClient.instance
    private val TAG = "LogTrailSDKTest"

    /**
     * Test sending a log to the backend
     */
    fun testSendLog() {
        val testLog = LogItem(
            userId = "test_user_123",
            level = "info",
            tag = "LogTrailSDKTest",
            message = "Test log message from Android SDK",
            timestamp = getCurrentTimestamp(),
            threadId = android.os.Process.myTid(),
            processId = android.os.Process.myPid(),
            packageName = "com.example.logtrailsdk"
        )

        api.sendLog(testLog).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "✅ SEND LOG TEST PASSED: ${response.body()}")
                } else {
                    Log.e(TAG, "❌ SEND LOG TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e(TAG, "❌ SEND LOG TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test getting all logs from the backend
     */
    fun testGetAllLogs() {
        api.getAllLogs().enqueue(object : Callback<List<LogItem>> {
            override fun onResponse(
                call: Call<List<LogItem>>,
                response: Response<List<LogItem>>
            ) {
                if (response.isSuccessful) {
                    val logs = response.body()
                    Log.d(TAG, "✅ GET ALL LOGS TEST PASSED: Found ${logs?.size ?: 0} logs")
                    logs?.forEach { log ->
                        Log.d(TAG, "Log: ${log.userId} - ${log.level} - ${log.tag} - ${log.message}")
                    }
                } else {
                    Log.e(TAG, "❌ GET ALL LOGS TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogItem>>, t: Throwable) {
                Log.e(TAG, "❌ GET ALL LOGS TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test getting filtered logs by userId
     */
    fun testGetFilteredLogsByUser(userId: String) {
        api.getLogs(userId = userId).enqueue(object : Callback<List<LogItem>> {
            override fun onResponse(
                call: Call<List<LogItem>>,
                response: Response<List<LogItem>>
            ) {
                if (response.isSuccessful) {
                    val logs = response.body()
                    Log.d(TAG, "✅ GET FILTERED LOGS BY USER TEST PASSED: Found ${logs?.size ?: 0} logs for user $userId")
                    logs?.forEach { log ->
                        Log.d(TAG, "User Log: ${log.userId} - ${log.level} - ${log.tag} - ${log.message}")
                    }
                } else {
                    Log.e(TAG, "❌ GET FILTERED LOGS BY USER TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogItem>>, t: Throwable) {
                Log.e(TAG, "❌ GET FILTERED LOGS BY USER TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test getting filtered logs by level
     */
    fun testGetFilteredLogsByLevel(level: String) {
        api.getLogs(level = level).enqueue(object : Callback<List<LogItem>> {
            override fun onResponse(
                call: Call<List<LogItem>>,
                response: Response<List<LogItem>>
            ) {
                if (response.isSuccessful) {
                    val logs = response.body()
                    Log.d(TAG, "✅ GET FILTERED LOGS BY LEVEL TEST PASSED: Found ${logs?.size ?: 0} logs with level $level")
                    logs?.forEach { log ->
                        Log.d(TAG, "Level Log: ${log.userId} - ${log.level} - ${log.tag} - ${log.message}")
                    }
                } else {
                    Log.e(TAG, "❌ GET FILTERED LOGS BY LEVEL TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogItem>>, t: Throwable) {
                Log.e(TAG, "❌ GET FILTERED LOGS BY LEVEL TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test getting filtered logs by date range
     */
    fun testGetFilteredLogsByDateRange(startDate: String, endDate: String) {
        api.getLogs(start = startDate, end = endDate).enqueue(object : Callback<List<LogItem>> {
            override fun onResponse(
                call: Call<List<LogItem>>,
                response: Response<List<LogItem>>
            ) {
                if (response.isSuccessful) {
                    val logs = response.body()
                    Log.d(TAG, "✅ GET FILTERED LOGS BY DATE RANGE TEST PASSED: Found ${logs?.size ?: 0} logs between $startDate and $endDate")
                    logs?.forEach { log ->
                        Log.d(TAG, "Date Log: ${log.userId} - ${log.level} - ${log.tag} - ${log.message} - ${log.timestamp}")
                    }
                } else {
                    Log.e(TAG, "❌ GET FILTERED LOGS BY DATE RANGE TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogItem>>, t: Throwable) {
                Log.e(TAG, "❌ GET FILTERED LOGS BY DATE RANGE TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test getting filtered logs by tag
     */
    fun testGetFilteredLogsByTag(tag: String) {
        api.getLogs(tag = tag).enqueue(object : Callback<List<LogItem>> {
            override fun onResponse(
                call: Call<List<LogItem>>,
                response: Response<List<LogItem>>
            ) {
                if (response.isSuccessful) {
                    val logs = response.body()
                    Log.d(TAG, "✅ GET FILTERED LOGS BY TAG TEST PASSED: Found ${logs?.size ?: 0} logs with tag $tag")
                    logs?.forEach { log ->
                        Log.d(TAG, "Tag Log: ${log.userId} - ${log.level} - ${log.tag} - ${log.message}")
                    }
                } else {
                    Log.e(TAG, "❌ GET FILTERED LOGS BY TAG TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogItem>>, t: Throwable) {
                Log.e(TAG, "❌ GET FILTERED LOGS BY TAG TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test getting filtered logs by package name
     */
    fun testGetFilteredLogsByPackageName(packageName: String) {
        api.getLogs(packageName = packageName).enqueue(object : Callback<List<LogItem>> {
            override fun onResponse(
                call: Call<List<LogItem>>,
                response: Response<List<LogItem>>
            ) {
                if (response.isSuccessful) {
                    val logs = response.body()
                    Log.d(TAG, "✅ GET FILTERED LOGS BY PACKAGE NAME TEST PASSED: Found ${logs?.size ?: 0} logs with package $packageName")
                    logs?.forEach { log ->
                        Log.d(TAG, "Package Log: ${log.userId} - ${log.level} - ${log.packageName} - ${log.message}")
                    }
                } else {
                    Log.e(TAG, "❌ GET FILTERED LOGS BY PACKAGE NAME TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LogItem>>, t: Throwable) {
                Log.e(TAG, "❌ GET FILTERED LOGS BY PACKAGE NAME TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Test sending a minimal log (only required fields)
     */
    fun testSendMinimalLog() {
        val minimalLog = LogItem(
            userId = "minimal_user_456",
            level = "warning",
            tag = null,
            message = "Minimal test log with only required fields",
            timestamp = getCurrentTimestamp(),
            threadId = null,
            processId = null,
            packageName = null
        )

        api.sendLog(minimalLog).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "✅ SEND MINIMAL LOG TEST PASSED: ${response.body()}")
                } else {
                    Log.e(TAG, "❌ SEND MINIMAL LOG TEST FAILED: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e(TAG, "❌ SEND MINIMAL LOG TEST FAILED: Network error - ${t.message}")
            }
        })
    }

    /**
     * Run all tests sequentially
     */
    fun runAllTests() {
        Log.d(TAG, "🚀 Starting LogTrail SDK Tests...")
        
        // Test 1: Send a full log
        testSendLog()
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 2: Send a minimal log
        testSendMinimalLog()
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 3: Get all logs
        testGetAllLogs()
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 4: Get logs filtered by user
        testGetFilteredLogsByUser("test_user_123")
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 5: Get logs filtered by level
        testGetFilteredLogsByLevel("info")
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 6: Get logs filtered by date range (last 7 days)
        val endDate = getCurrentDate()
        val startDate = getDateDaysAgo(7)
        testGetFilteredLogsByDateRange(startDate, endDate)
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 7: Get logs filtered by tag
        testGetFilteredLogsByTag("LogTrailSDKTest")
        
        // Wait a bit between tests
        Thread.sleep(1000)
        
        // Test 8: Get logs filtered by package name
        testGetFilteredLogsByPackageName("com.example.logtrailsdk")
        
        Log.d(TAG, "🏁 All LogTrail SDK Tests initiated!")
    }

    /**
     * Helper function to get current timestamp in ISO format
     */
    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Helper function to get current date in YYYY-MM-DD format
     */
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Helper function to get date X days ago in YYYY-MM-DD format
     */
    private fun getDateDaysAgo(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }
} 