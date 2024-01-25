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

data class MemoryUsage(
    var usedMemoryInBytes: Long = 0L,
    var gcCount: Long = 0L
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
private fun getSafeMemoryUsage(): MemoryUsage {
    val before = getGcCount()
    ManagementFactory.getMemoryMXBean().gc()
    while (getGcCount() == before);
    val memoryUsage = MemoryUsage(
        gcCount = getGcCount()
    )
    memoryUsage.usedMemoryInBytes = ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
    return memoryUsage
}

fun nooraEye(
    title: String,
    block: () -> Unit
): EyeResult  {
    if (lock.isLocked) {
        throw Exception("nooraEye shouldn't run more than one execution at a time")
    }
    lock.lock()
    var maximumReachedHeapMemoryInByte = 0L
    val trackerThread = Thread {
        try {
            while (true) {
                val usedHeap = ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
                if (usedHeap > maximumReachedHeapMemoryInByte) {
                    maximumReachedHeapMemoryInByte = usedHeap
                }
                Thread.sleep(100)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        } catch (e: OutOfMemoryError){
            Thread.currentThread().interrupt()
        }
    }
    trackerThread.start()
    val beforeExecutionTimestamp = System.currentTimeMillis()
    val memoryUsageBeforeExecution = getSafeMemoryUsage()
    var afterUnsafeMemoryUsage: Long = 0
    var afterExecutionTimestamp: Long = 0
    try {
        block().also {
            afterUnsafeMemoryUsage = ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
            afterExecutionTimestamp = System.currentTimeMillis()
        }
    } catch (exception: OutOfMemoryError) {
        lock.unlock()
        trackerThread.interrupt()
        return EyeResult(
            title = title,
            isRanOutOfMemory = true,
            maximumReachedHeapMemoryInByte = maximumReachedHeapMemoryInByte
        )
    }
    var isAccurate = true
    var gcTriggerCount = 0L
    val memoryUsageAfterExecution = MemoryUsage(
        usedMemoryInBytes = afterUnsafeMemoryUsage,
        gcCount = getGcCount()
    )
    if (memoryUsageAfterExecution.gcCount != memoryUsageBeforeExecution.gcCount) {
        isAccurate = false
        gcTriggerCount = memoryUsageAfterExecution.gcCount - memoryUsageBeforeExecution.gcCount
    }
    var memoryUsage = memoryUsageAfterExecution.usedMemoryInBytes - memoryUsageBeforeExecution.usedMemoryInBytes
    if (memoryUsage < 0) {
        memoryUsage = 0
    }
    lock.unlock()
    trackerThread.interrupt()
    return EyeResult(
        title = title,
        memoryUsageInByte = memoryUsage,
        executionDurationInMs = afterExecutionTimestamp - beforeExecutionTimestamp,
        isMemoryAccurate = isAccurate,
        gcTriggerCount = gcTriggerCount
    )
}

fun printComparison(a: EyeResult, b: EyeResult) {
    var memoryComparison = ""
    var timeComparison = ""
    if (a.memoryUsageInByte < b.memoryUsageInByte) {
        val difference: Double =
            (b.memoryUsageInByte.toDouble() / a.memoryUsageInByte.toDouble()).floorWithTwoDecimal()
        memoryComparison = "${a.title} allocates ${difference}x less memory"
    }
    if (a.memoryUsageInByte > b.memoryUsageInByte) {
        val difference: Double =
            (a.memoryUsageInByte.toDouble() / b.memoryUsageInByte.toDouble()).floorWithTwoDecimal()
        memoryComparison = "${b.title} allocates ${difference}x less memory"
    }
    if (a.executionDurationInMs < b.executionDurationInMs) {
        val difference: Double =
            (b.executionDurationInMs.toDouble() / a.executionDurationInMs.toDouble()).floorWithTwoDecimal()
        timeComparison = "${a.title} executed ${difference}x faster"
    }
    if (a.executionDurationInMs > b.executionDurationInMs) {
        val difference: Double =
            (a.executionDurationInMs.toDouble() / b.executionDurationInMs.toDouble()).floorWithTwoDecimal()
        timeComparison = "${b.title} executed ${difference}x faster"
    }
    val length = if (memoryComparison.length > timeComparison.length) memoryComparison.length else timeComparison.length
    ConsolePrinter(length).run {
        printFirstLine()
        printLine(memoryComparison)
        printLine(timeComparison)
        printLastLine()
    }
}
