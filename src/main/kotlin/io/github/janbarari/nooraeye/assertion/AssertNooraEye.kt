package io.github.janbarari.nooraeye.assertion

import io.github.janbarari.nooraeye.functionEye

fun assertFunctionEye(title: String, memoryThresholdInByte: Long, timeThresholdInMs: Long, block: () -> Unit) {
    val eyeResult = functionEye(title, block)
    if (eyeResult.partialMemoryUsageInByte > memoryThresholdInByte) {
        throw FunctionEyeAssertionException(
            ("Extra Memory Used! Target threshold was %s bytes but it used %s bytes")
                .format(
                    memoryThresholdInByte,
                    eyeResult.partialMemoryUsageInByte
                )
        )
    }
    if (eyeResult.executionDurationInMs > timeThresholdInMs) {
        throw FunctionEyeAssertionException("Extra Time Used! Target threshold was %s ms but it took %s ms"
                    .format(
                        timeThresholdInMs,
                        eyeResult.executionDurationInMs
                    )
        )
    }
}
