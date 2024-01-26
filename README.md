# Noora Eye (چشم نورا)
[![](https://jitpack.io/v/janbarari/NooraEye.svg)](https://jitpack.io/#janbarari/NooraEye)

NooraEye seamlessly measures memory usage, execution time, and provides memory leak information for your Kotlin algorithms and code blocks.

Installation
------------
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.janbarari:NooraEye:0.1-beta5'
```

Usage
-----
```kotlin
nooraEye(title = "Test #1") {
    // put your codes here
}.prettyPrint(
    memoryFormatter = MemoryFormatters.mb,
    timeFormatter = TimeFormatters.millis
)
```
Output
-----
<img width="369" alt="Screenshot 2024-01-25 at 10 58 43 PM" src="https://github.com/janbarari/NooraEye/assets/12547060/d79d66fd-b7e5-4b72-81d0-cef28803939c">


Unit Tests
------
```kotlin
assertThrows<NooraEyeExceedMemoryException> {
    assertNooraEye("My Test 1", memoryThresholdInByte = 1000, timeThresholdInMs = 1000) {
        // Fails when memory usage exceeds the threshold
    }
}

assertThrows<NooraEyeExceedExecutionException> {
    assertNooraEye("My Test 2", memoryThresholdInByte = 1000, timeThresholdInMs = 1000) {
        // Fails when execution took longer than threshold
    }
}

assertThrows<NooraEyeRanOutOfMemoryException> {
    assertNooraEye("My Test 3", memoryThresholdInByte = 1000, timeThresholdInMs = 1000) {
        // Fail when process ran out of memory
    }
}
```

License
-------
Copyright (C) 2024 Mehdi Janbarari (@janbarari)

NooraEye binaries and source code can be used according to the [MIT License](LICENSE).
