package com.example.logtrail_sdk.Data


data class LogItem(
    val userId: String,
    val level: String,
    val tag: String?,        // e.g. "MainActivity"
    val message: String,
    val timestamp: String,
    val threadId: Int?,      // for concurrency debugging
    val processId: Int?,     // helps in multi-process apps
    val packageName: String? // optional, useful if SDK used in multiple apps
)