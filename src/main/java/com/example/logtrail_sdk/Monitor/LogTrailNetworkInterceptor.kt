package com.example.logtrail_sdk.Monitor

import android.util.Log
import com.example.logtrail_sdk.Logger.LogTrailLogger
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * LogTrailNetworkInterceptor - Monitors network requests and responses
 * 
 * OkHttp interceptor that logs HTTP failures, slow requests, and other
 * network-related issues to help with debugging connectivity problems.
 */
class LogTrailNetworkInterceptor : Interceptor {
    
    private val TAG = "LogTrailNetworkInterceptor"
    
    // Configuration for what constitutes a "slow" request
    private val slowRequestThresholdMs = 3000L // 3 seconds
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val method = request.method()
        
        // Skip logging requests to LogTrail backend to prevent feedback loop
        if (isLogTrailBackendRequest(url)) {
            return chain.proceed(request)
        }
        
        // Start timing the request
        val startTime = System.currentTimeMillis()
        
        var response: Response? = null
        var exception: Exception? = null
        
        try {
            // Execute the request
            response = chain.proceed(request)
            
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            
            // Log based on response status and duration
            logNetworkResponse(method, url, response, duration)
            
            return response
            
        } catch (e: Exception) {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            
            exception = e
            logNetworkFailure(method, url, e, duration)
            
            // Re-throw the exception to maintain normal error handling
            throw e
        }
    }
    
    /**
     * Check if the request is to LogTrail backend to prevent logging feedback loop
     */
    private fun isLogTrailBackendRequest(url: String): Boolean {
        return url.contains("/logs/") || url.contains("/logs")
    }
    
    /**
     * Log network response based on status and performance
     */
    private fun logNetworkResponse(method: String, url: String, response: Response, duration: Long) {
        try {
            val statusCode = response.code()
            val statusMessage = response.message()
            val contentLength = response.body()?.contentLength() ?: -1
            
            when {
                // Log errors (4xx, 5xx)
                statusCode >= 400 -> {
                    val errorMessage = "$method $url failed with status $statusCode ($statusMessage) in ${duration}ms"
                    LogTrailLogger.e("Network", errorMessage)
                    
                    // Log additional error details
                    logNetworkErrorDetails(method, url, statusCode, statusMessage, duration)
                }
                
                // Log slow successful requests
                duration > slowRequestThresholdMs -> {
                    val slowMessage = "$method $url completed in ${duration}ms (SLOW) - Status: $statusCode"
                    LogTrailLogger.w("Network", slowMessage)
                }
                
                // Log successful requests at debug level
                else -> {
                    val successMessage = "$method $url - Status: $statusCode in ${duration}ms"
                    LogTrailLogger.d("Network", successMessage)
                }
            }
            
            // Log large responses that might indicate inefficient API usage
            if (contentLength > 1024 * 1024) { // > 1MB
                val sizeWarning = "$method $url returned large response: ${contentLength / 1024 / 1024}MB"
                LogTrailLogger.w("NetworkSize", sizeWarning)
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error logging network response: ${e.message}")
        }
    }
    
    /**
     * Log network failure details
     */
    private fun logNetworkFailure(method: String, url: String, exception: Exception, duration: Long) {
        try {
            val exceptionType = exception.javaClass.simpleName
            val exceptionMessage = exception.message ?: "No message"
            
            val failureMessage = "$method $url failed after ${duration}ms - $exceptionType: $exceptionMessage"
            LogTrailLogger.e("Network", failureMessage)
            
            // Categorize the network error
            val errorCategory = categorizeNetworkError(exception)
            LogTrailLogger.e("NetworkError", "$errorCategory - $method $url")
            
            // Log specific error types with additional context
            when (exception) {
                is java.net.SocketTimeoutException -> {
                    LogTrailLogger.e("NetworkTimeout", "Request timeout for $method $url after ${duration}ms")
                }
                is java.net.UnknownHostException -> {
                    LogTrailLogger.e("NetworkDNS", "DNS resolution failed for $url")
                }
                is java.net.ConnectException -> {
                    LogTrailLogger.e("NetworkConnection", "Connection failed for $url")
                }
                is javax.net.ssl.SSLException -> {
                    LogTrailLogger.e("NetworkSSL", "SSL/TLS error for $url: $exceptionMessage")
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error logging network failure: ${e.message}")
        }
    }
    
    /**
     * Log additional details for HTTP error responses
     */
    private fun logNetworkErrorDetails(method: String, url: String, statusCode: Int, statusMessage: String, duration: Long) {
        try {
            val errorCategory = when (statusCode) {
                in 400..499 -> "CLIENT_ERROR"
                in 500..599 -> "SERVER_ERROR"
                else -> "HTTP_ERROR"
            }
            
            LogTrailLogger.e("NetworkHTTP", "$errorCategory - $statusCode $statusMessage for $method $url")
            
            // Log specific common errors
            when (statusCode) {
                400 -> LogTrailLogger.e("NetworkBadRequest", "Bad Request - $method $url")
                401 -> LogTrailLogger.e("NetworkAuth", "Unauthorized - $method $url (check authentication)")
                403 -> LogTrailLogger.e("NetworkAuth", "Forbidden - $method $url (check permissions)")
                404 -> LogTrailLogger.e("NetworkNotFound", "Not Found - $method $url")
                408 -> LogTrailLogger.e("NetworkTimeout", "Request Timeout - $method $url after ${duration}ms")
                429 -> LogTrailLogger.e("NetworkRateLimit", "Rate Limited - $method $url")
                500 -> LogTrailLogger.e("NetworkServer", "Internal Server Error - $method $url")
                502 -> LogTrailLogger.e("NetworkGateway", "Bad Gateway - $method $url")
                503 -> LogTrailLogger.e("NetworkServer", "Service Unavailable - $method $url")
                504 -> LogTrailLogger.e("NetworkTimeout", "Gateway Timeout - $method $url")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error logging network error details: ${e.message}")
        }
    }
    
    /**
     * Categorize network errors for analytics
     */
    private fun categorizeNetworkError(exception: Exception): String {
        return when (exception) {
            is java.net.SocketTimeoutException -> "TIMEOUT"
            is java.net.UnknownHostException -> "DNS_FAILURE"
            is java.net.ConnectException -> "CONNECTION_FAILURE"
            is javax.net.ssl.SSLException -> "SSL_ERROR"
            is java.net.SocketException -> "SOCKET_ERROR"
            is IOException -> "IO_ERROR"
            else -> "UNKNOWN_NETWORK_ERROR"
        }
    }
} 