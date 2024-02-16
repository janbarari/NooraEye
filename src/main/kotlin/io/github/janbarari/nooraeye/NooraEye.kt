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

import oshi.SystemInfo
import oshi.hardware.CentralProcessor
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
    block: EyeProgress.() -> Unit
): EyeResult {
    if (lock.isLocked) {
        throw Exception("nooraEye shouldn't run more than one operation at a time")
    }
    lock.lock()
    val eyeProgress = EyeProgress(title)
    val hardware = SystemInfo().hardware
    val cpu = hardware.processor
    var prevTicks = LongArray(CentralProcessor.TickType.entries.size)
    var maximumReachedHeapMemoryInByte = 0L
    var averageCpuLoad = 0.0
    var maxCpuLoad = -1.0

    var beforeIOReadSizeInByte: Long
    var beforeIOWriteSizeInByte: Long
    var beforeIOReadOps: Long
    var beforeIOWriteOps: Long
    hardware.diskStores.run {
        beforeIOReadSizeInByte = sumOf { it.readBytes }
        beforeIOWriteSizeInByte = sumOf { it.writeBytes }
        beforeIOReadOps = sumOf { it.reads }
        beforeIOWriteOps = sumOf { it.writes }
    }

    var afterIOReadSizeInByte = 0L
    var afterIOWriteSizeInByte = 0L
    var afterIOReadOps = 0L
    var afterIOWriteOps = 0L

    var isTrackerActive = true
    val trackerThread = Thread {
        try {
            while (isTrackerActive) {
                val usedHeap = ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
                if (usedHeap > maximumReachedHeapMemoryInByte) {
                    maximumReachedHeapMemoryInByte = usedHeap
                }

                hardware.diskStores.run {
                    afterIOReadSizeInByte = sumOf { it.readBytes }
                    afterIOWriteSizeInByte = sumOf { it.writeBytes }
                    afterIOReadOps = sumOf { it.reads }
                    afterIOWriteOps = sumOf { it.writes }
                }

                val cpuLoad = cpu.getSystemCpuLoadBetweenTicks(prevTicks) * 100
                prevTicks = cpu.systemCpuLoadTicks
                if (cpuLoad > 0.0) {
                    averageCpuLoad = (averageCpuLoad + cpuLoad) / 2.0
                    if (maxCpuLoad == 0.0 || maxCpuLoad < averageCpuLoad) {
                        maxCpuLoad = averageCpuLoad
                    }
                    if (cpuLoad > (cpuLoad * 15 / 100) + averageCpuLoad) {
                        maxCpuLoad = (maxCpuLoad + cpuLoad) / 2
                    }
                }

                Thread.sleep(50)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        } catch (e: OutOfMemoryError) {
            Thread.currentThread().interrupt()
        }
    }
    trackerThread.start()
    val beforeExecutionTimestamp = System.currentTimeMillis()
    val memoryUsageBeforeExecution = getSafeMemoryUsage()
    var afterUnsafeMemoryUsage: Long
    var afterExecutionTimestamp: Long
    try {
        block(eyeProgress).apply {
            isTrackerActive = false
            afterUnsafeMemoryUsage = ManagementFactory.getMemoryMXBean().heapMemoryUsage.used
            afterExecutionTimestamp = System.currentTimeMillis()
        }
    } catch (exception: OutOfMemoryError) {
        lock.unlock()
        trackerThread.interrupt()
        if (eyeProgress.isEyeProgressInitiated) {
            eyeProgress.setProgress(100)
        }
        return EyeResult(
            title = title,
            isRanOutOfMemory = true,
            maxReachedHeapMemoryInByte = maximumReachedHeapMemoryInByte
        )
    }
    trackerThread.interrupt()
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
    return EyeResult(
        title = title,

        averageCpuLoad = averageCpuLoad,
        maxCpuLoad = maxCpuLoad,

        memoryUsageInByte = memoryUsage,
        maxReachedHeapMemoryInByte = maximumReachedHeapMemoryInByte,
        isMemoryAccurate = isAccurate,
        gcTriggerCount = gcTriggerCount,

        ioReadSizeInByte = afterIOReadSizeInByte - beforeIOReadSizeInByte,
        ioWriteSizeInByte = afterIOWriteSizeInByte - beforeIOWriteSizeInByte,
        ioReadOps = afterIOReadOps - beforeIOReadOps,
        ioWriteOps = afterIOWriteOps - beforeIOWriteOps,

        executionDurationInMs = afterExecutionTimestamp - beforeExecutionTimestamp,
    )
}
