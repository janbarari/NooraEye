package io.github.janbarari.nooraeye.assertion

import io.github.janbarari.nooraeye.nooraEye

fun assertNooraEye(title: String, memoryThresholdInByte: Long, timeThresholdInMs: Long, block: () -> Unit) {
    val eyeResult = nooraEye(title, block)
    if (eyeResult.partialMemoryUsageInByte > memoryThresholdInByte) {
        throw NooraEyeAssertionException(
            ("Extra Memory Used! Target threshold was %s bytes but it used %s bytes")
                .format(
                    memoryThresholdInByte,
                    eyeResult.partialMemoryUsageInByte
                )
        )
    }
    if (eyeResult.executionDurationInMs > timeThresholdInMs) {
        throw NooraEyeAssertionException("Extra Time Used! Target threshold was %s ms but it took %s ms"
                    .format(
                        timeThresholdInMs,
                        eyeResult.executionDurationInMs
                    )
        )
    }
}
