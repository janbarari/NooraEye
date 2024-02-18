/**
 * MIT License
 * Copyright (c) 2022 Mehdi Janbarari (@janbarari)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("AssertNooraEye")

package io.github.janbarari.nooraeye.assertion

import io.github.janbarari.nooraeye.EyeProgress
import io.github.janbarari.nooraeye.NooraEyeMultipleOperationException
import io.github.janbarari.nooraeye.memory.MemoryFormatters
import io.github.janbarari.nooraeye.nooraEye

@Throws(
    NooraEyeMultipleOperationException::class,
    NooraEyeRanOutOfMemoryException::class,
    NooraEyeExceedMemoryException::class,
    NooraEyeExceedExecutionException::class
)
fun assertNooraEye(title: String, memoryThresholdInByte: Long, timeThresholdInMs: Long, block: EyeProgress.() -> Unit) {
    val eyeResult = nooraEye(title, block)
        .apply {
            prettyPrint(
                memoryFormatter = MemoryFormatters.kb
            )
        }

    if (eyeResult.isRanOutOfMemory) {
        throw NooraEyeRanOutOfMemoryException(eyeResult.maxReachedHeapMemoryInByte)
    }

    if (eyeResult.memoryUsageInByte > memoryThresholdInByte) {
        throw NooraEyeExceedMemoryException(memoryThresholdInByte, eyeResult.memoryUsageInByte)
    }

    if (eyeResult.executionDurationInMs > timeThresholdInMs) {
        throw NooraEyeExceedExecutionException(timeThresholdInMs, eyeResult.executionDurationInMs)
    }
}
