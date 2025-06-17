package com.example.logtrail_sdk.Data

/**
 * Configuration class for LogTrail SDK initialization
 * 
 * @param baseUrl The base URL for the LogTrail API (required)
 * @param userId The user ID for logging (required)
 * @param enableMonitoring Whether to enable automatic monitoring features (default: true)
 * @param enableDebugLogs Whether to enable debug logging within the SDK (default: false)
 * @param connectionTimeoutSeconds HTTP connection timeout in seconds (default: 30)
 * @param readTimeoutSeconds HTTP read timeout in seconds (default: 30)
 */
data class LogTrailConfig(
    val baseUrl: String,
    val userId: String,
    val enableMonitoring: Boolean = true,
    val enableDebugLogs: Boolean = false,
    val connectionTimeoutSeconds: Long = 30,
    val readTimeoutSeconds: Long = 30
) {
    init {
        require(baseUrl.isNotBlank()) { "baseUrl cannot be blank" }
        require(userId.isNotBlank()) { "userId cannot be blank" }
        require(connectionTimeoutSeconds > 0) { "connectionTimeoutSeconds must be positive" }
        require(readTimeoutSeconds > 0) { "readTimeoutSeconds must be positive" }
    }
    
    companion object {
        /**
         * Normalizes the base URL by ensuring it ends with a slash
         */
        private fun normalizeBaseUrl(baseUrl: String): String {
            return if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        }
        
        /**
         * Creates a basic LogTrailConfig with required parameters
         * @param baseUrl The base URL for the LogTrail API
         * @param userId The user ID for logging
         */
        fun create(baseUrl: String, userId: String): LogTrailConfig {
            return LogTrailConfig(baseUrl = normalizeBaseUrl(baseUrl), userId = userId)
        }
        
        /**
         * Creates a LogTrailConfig with monitoring disabled
         * @param baseUrl The base URL for the LogTrail API
         * @param userId The user ID for logging
         */
        fun createWithoutMonitoring(baseUrl: String, userId: String): LogTrailConfig {
            return LogTrailConfig(baseUrl = normalizeBaseUrl(baseUrl), userId = userId, enableMonitoring = false)
        }
        
        /**
         * Creates a LogTrailConfig with debug logging enabled
         * @param baseUrl The base URL for the LogTrail API
         * @param userId The user ID for logging
         */
        fun createWithDebugLogs(baseUrl: String, userId: String): LogTrailConfig {
            return LogTrailConfig(baseUrl = normalizeBaseUrl(baseUrl), userId = userId, enableDebugLogs = true)
        }
        
        /**
         * Creates a LogTrailConfig with custom timeout settings
         * @param baseUrl The base URL for the LogTrail API
         * @param userId The user ID for logging
         * @param connectionTimeoutSeconds HTTP connection timeout in seconds
         * @param readTimeoutSeconds HTTP read timeout in seconds
         */
        fun createWithCustomTimeouts(
            baseUrl: String, 
            userId: String,
            connectionTimeoutSeconds: Long,
            readTimeoutSeconds: Long
        ): LogTrailConfig {
            return LogTrailConfig(
                baseUrl = normalizeBaseUrl(baseUrl), 
                userId = userId,
                connectionTimeoutSeconds = connectionTimeoutSeconds,
                readTimeoutSeconds = readTimeoutSeconds
            )
        }
    }
} 