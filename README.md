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

implementation 'com.github.janbarari:NooraEye:0.1-beta3'
```

Usage
-----
```kotlin
nooraEye(title = "My Code") {
    // put your codes here
}.prettyPrint(
    memoryFormatter = MemoryFormatters.mb,
    timeFormatter = TimeFormatters.millis
)
```

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