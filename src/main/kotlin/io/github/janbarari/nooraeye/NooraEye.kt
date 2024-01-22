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

package io.github.janbarari.nooraeye

import java.lang.management.ManagementFactory

private val systemRuntime = Runtime.getRuntime()

data class RealMemoryResult(
    val usedMemoryInBytes: Long,
    val gcCount: Long
)

/**
 * Returns the memory gc count
 */
private fun getGcCount(): Long {
    var sum: Long = 0
    for (b in ManagementFactory.getGarbageCollectorMXBeans()) {
        val count = b.collectionCount
        if (count != -1L) {
            sum += count
        }
    }
    return sum
}

/**
 * Returns really used memory by comparing the gc count before and after System.gc() invoked.
 */
private fun getReallyUsedMemory(): RealMemoryResult {
    val before = getGcCount()
    System.gc()
    while (getGcCount() == before);
    return RealMemoryResult(
        usedMemoryInBytes = systemRuntime.usedMemory(),
        gcCount = getGcCount()
    )
}

fun nooraEye(
    title: String,
    block: () -> Unit
): EyeResult {
    if (lock.isLocked) {
        throw Exception("nooraEye shouldn't run more than one execution at a time")
    }
    lock.lock()
    val memoryUsageBeforeExecution = getReallyUsedMemory()
    val timestampBeforeExecution = System.currentTimeMillis()
    block()
    var isAccurate = true
    val timestampAfterExecution = System.currentTimeMillis()
    val memoryUsageAfterExecution = RealMemoryResult(
        usedMemoryInBytes = systemRuntime.usedMemory(),
        gcCount = getGcCount()
    )
    if (memoryUsageAfterExecution.gcCount != memoryUsageBeforeExecution.gcCount) {
        isAccurate = false
    }
    var partialMemoryUsage = memoryUsageAfterExecution.usedMemoryInBytes - memoryUsageBeforeExecution.usedMemoryInBytes
    if (partialMemoryUsage < 0) {
        partialMemoryUsage = 0
    }
    lock.unlock()
    return EyeResult(
        title = title,
        partialMemoryUsageInByte = partialMemoryUsage,
        executionDurationInMs = timestampAfterExecution - timestampBeforeExecution,
        isAccurate = isAccurate
    )
}
