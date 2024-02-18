# Noora Eye (چشم نورا)
[![](https://jitpack.io/v/janbarari/NooraEye.svg)](https://jitpack.io/#janbarari/NooraEye)

NooraEye seamlessly measures memory usage, CPU usage, IO usage, execution time, and provides memory leak information for your Kotlin/Java algorithms and code blocks.

What's the point of using it
-----
Struggling with slow apps and mysterious memory, disk(IO), CPU problems? NooraEye is your new best friend! It easily checks your code.

Think of it this time you spent writing beautiful code, but it runs slow. NooraEye shows you why, like spotting memory spikes or long-running operations. Its simple interface helps you fix these issues quickly.

This means faster development, cleaner code, and happier users! NooraEye gives you the data you need to make smart choices, optimize your app, and make it run like a champ.

Ditch the guessing, embrace the power, and unlock your expertise with NooraEye!

Installation
------------
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.janbarari:NooraEye:0.1-beta7'
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
<img width="500" alt="Screenshot 2024-01-25 at 10 58 43 PM" src="https://github.com/janbarari/NooraEye/assets/12547060/08de0a95-8be2-41d6-8ebc-fc748bb340d9">


Unit Tests
------
```kotlin
assertDoesNotThrow<NooraEyeExceedMemoryException> {
    assertNooraEye("My Test 1", memoryThresholdInByte = 1000, timeThresholdInMs = 1000) {
        // Fails when memory usage exceeds the threshold
    }
}

assertDoesNotThrow<NooraEyeExceedExecutionException> {
    assertNooraEye("My Test 2", memoryThresholdInByte = 1000, timeThresholdInMs = 1000) {
        // Fails when execution took longer than threshold
    }
}

assertDoesNotThrow<NooraEyeRanOutOfMemoryException> {
    assertNooraEye("My Test 3", memoryThresholdInByte = 1000, timeThresholdInMs = 1000) {
        // Fail when process ran out of memory
    }
}
```

License
-------
Copyright (C) 2024 Mehdi Janbarari (@janbarari)

NooraEye binaries and source code can be used according to the [MIT License](LICENSE).

نور چشمانش امید روزهای تلخ من بود 🖤
