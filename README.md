# LogTrail SDK

[![](https://jitpack.io/v/YourUsername/LogTrailSDK.svg)](https://jitpack.io/#YourUsername/LogTrailSDK)

A lightweight, self-hosted Android logging SDK that automatically captures logs, crashes, lifecycle events, and network failures. Send logs to your own backend infrastructure with real-time visualization through a web dashboard.

## 🚀 Features

- **Custom Logging**: Easy-to-use `LogTrailLogger` with standard log levels (DEBUG, INFO, WARN, ERROR, VERBOSE)
- **Automatic Monitoring**: Silent capture of system events without code changes
  - 🔴 Uncaught exceptions and crashes
  - 📱 Activity lifecycle events
  - 🌐 Network request failures and timeouts
- **Self-Hosted**: Keep your logs on your own infrastructure
- **Real-time Dashboard**: Visualize logs through the companion web interface
- **Retrofit Integration**: Built-in HTTP client with configurable endpoints
- **Minimal Setup**: Single line initialization
- **Thread-Safe**: Concurrent logging without blocking your UI

## 📦 Installation

### Step 1: Add JitPack repository

Add JitPack to your project's `settings.gradle.kts` (or root `build.gradle`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Add this line
    }
}
```

### Step 2: Add the dependency

Add to your app-level `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.liadb33:LogTrailSDK:1.0.0")
}
```

## 🛠️ Setup

### Basic Initialization

Initialize the SDK in your `Application` class or `MainActivity`:

```kotlin
import com.example.logtrail_sdk.Utilities.LogTrail
import com.example.logtrail_sdk.Data.LogTrailConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize LogTrail SDK
        val config = LogTrailConfig.create(
            baseUrl = "https://your-backend.com/api/",
            userId = "user123"
        )
        
        LogTrail.init(this, config)
    }
}
```

### Advanced Configuration

```kotlin
val config = LogTrailConfig(
    baseUrl = "https://your-backend.com/api/",
    userId = "user123",
    enableMonitoring = true,        // Auto-capture crashes & lifecycle
    enableDebugLogs = false,        // SDK internal logging
    connectionTimeoutSeconds = 30,   // HTTP timeout
    readTimeoutSeconds = 30         // HTTP read timeout
)

LogTrail.init(this, config)
```

### Configuration Options

Create configs with helper methods:

```kotlin
// Basic setup
val config = LogTrailConfig.create("https://api.yourapp.com/", "user123")

// Disable automatic monitoring
val config = LogTrailConfig.createWithoutMonitoring("https://api.yourapp.com/", "user123")

// Enable debug logging
val config = LogTrailConfig.createWithDebugLogs("https://api.yourapp.com/", "user123")

// Custom timeouts
val config = LogTrailConfig.createWithCustomTimeouts(
    baseUrl = "https://api.yourapp.com/",
    userId = "user123",
    connectionTimeoutSeconds = 60,
    readTimeoutSeconds = 60
)
```

## 📝 Usage

### Manual Logging

Use `LogTrailLogger` just like Android's `Log` class:

```kotlin
import com.example.logtrail_sdk.Logger.LogTrailLogger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Debug logs
        LogTrailLogger.d("MainActivity", "Activity created successfully")
        
        // Info logs
        LogTrailLogger.i("UserAction", "User tapped login button")
        
        // Warning logs
        LogTrailLogger.w("NetworkManager", "Slow network detected")
        
        // Error logs
        LogTrailLogger.e("LoginService", "Authentication failed")
        
        // Error with exception
        try {
            riskyOperation()
        } catch (e: Exception) {
            LogTrailLogger.e("MainActivity", "Operation failed", e)
        }
        
        // Verbose logs
        LogTrailLogger.v("DataProcessor", "Processing 1000 records...")
    }
}
```

### Automatic Monitoring

The SDK automatically captures:

**🔴 Uncaught Exceptions**
```kotlin
// This crash will be automatically logged
throw RuntimeException("Something went wrong!")
// Appears in dashboard as: ERROR | UncaughtException | Something went wrong!
```

**📱 Activity Lifecycle**
```kotlin
// These are logged automatically:
// INFO | ActivityLifecycle | MainActivity - onCreate
// INFO | ActivityLifecycle | MainActivity - onStart  
// INFO | ActivityLifecycle | MainActivity - onResume
// INFO | ActivityLifecycle | MainActivity - onPause
```

**🌐 Network Failures**
```kotlin
// Retrofit integration captures failed requests automatically
// ERROR | NetworkInterceptor | HTTP 500 - POST /api/login - Connection timeout
```

## 🔧 Backend Integration

Your backend should expect POST requests to `/api/logs` with this payload:

```json
{
  "userId": "user123",
  "level": "error",
  "tag": "MainActivity", 
  "message": "Login failed due to timeout",
  "timestamp": "2024-01-15 14:30:25.123",
  "threadId": 2,
  "processId": 1234,
  "packageName": "com.yourapp.android"
}
```

### Sample Express.js Backend

```javascript
app.post('/api/logs', (req, res) => {
  const { userId, level, tag, message, timestamp } = req.body;
  
  // Store in your database
  console.log(`[${timestamp}] ${level.toUpperCase()} ${tag}: ${message}`);
  
  res.json({ status: 'success' });
});
```

## 🎯 Best Practices

1. **Initialize Early**: Call `LogTrail.init()` in your `Application.onCreate()`
2. **Use Descriptive Tags**: Help identify log sources easily
3. **Log Meaningful Events**: User actions, errors, and state changes
4. **Secure Your Endpoint**: Use HTTPS and authentication for production
5. **Monitor Performance**: The SDK is non-blocking, but avoid excessive logging

## 🐛 Troubleshooting

### SDK Not Sending Logs?

```kotlin
// Check if SDK is initialized
if (!LogTrailLogger.isReady()) {
    Log.w("MyApp", "LogTrail SDK not ready!")
}
```

### Network Issues?

Enable debug logs to see HTTP requests:

```kotlin
val config = LogTrailConfig.createWithDebugLogs("https://api.yourapp.com/", "user123")
LogTrail.init(this, config)
```

### Missing Automatic Events?

Ensure monitoring is enabled:

```kotlin
val config = LogTrailConfig(
    baseUrl = "https://api.yourapp.com/",
    userId = "user123", 
    enableMonitoring = true  // Must be true
)
```

## 📋 Requirements

- **Android API 26+** (Android 8.0)
- **Kotlin/Java** compatible
- **Internet permission** in `AndroidManifest.xml`:
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />
  ```

## 🔗 Related Projects

- **LogTrail Platform**: [LogTrail Platform - Backend & Frontend](https://github.com/liadb33/LogTrailPlatform)


---

**Made with ❤️ for Android developers who want better logging**
