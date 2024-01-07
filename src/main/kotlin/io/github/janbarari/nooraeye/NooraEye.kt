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

import java.math.RoundingMode
import java.text.DecimalFormat

fun nooraEye(
    title: String,
    block: () -> Unit
): EyeResult {
    val systemRuntime = Runtime.getRuntime()
    runGarbageCollector(systemRuntime)
    val memoryUsageBeforeExecution = systemRuntime.usedMemory()
    val timestampBeforeExecution = System.currentTimeMillis()
    block()
    val timestampAfterExecution = System.currentTimeMillis()
    val memoryUsageAfterExecution = systemRuntime.usedMemory()

    return EyeResult(
        title = title,
        partialMemoryUsageInByte = memoryUsageAfterExecution - memoryUsageBeforeExecution,
        executionDurationInMs = timestampAfterExecution - timestampBeforeExecution
    )
}

data class EyeResult(
    val title: String,
    val partialMemoryUsageInByte: Long,
    val executionDurationInMs: Long
)

private fun runGarbageCollector(systemRuntime: Runtime) {
    (0..<3).forEach { _ -> systemRuntime.gc() }
    Thread.sleep(500)
}

private fun Runtime.usedMemory(): Long = totalMemory() - freeMemory()

interface MemoryFormatter {
    fun format(value: Long): String
}

class MemoryByteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sB".format(value)
    }
}

class MemoryKilobyteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sKb".format(value.toKb())
    }
}

class MemoryMegabyteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sMb".format(value.toMb())
    }
}

interface TimeFormatter {
    fun format(value: Long): String
}

class TimeMillisecondFormatter : TimeFormatter {
    override fun format(value: Long): String {
        return "%sms".format(value)
    }
}

class TimeSecondFormatter : TimeFormatter {
    override fun format(value: Long): String {
        return "%ss".format(value.toSecond())
    }
}


val B: MemoryFormatter = MemoryByteFormatter()
val KB: MemoryFormatter = MemoryKilobyteFormatter()
val MB: MemoryFormatter = MemoryMegabyteFormatter()

fun Double.floorWithTwoDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toDouble()
}

fun Long.toKb(): Double = (this / 1024.0).floorWithTwoDecimal()
fun Long.toMb(): Double = (this / 1048576.0).floorWithTwoDecimal()

val M: TimeFormatter = TimeMillisecondFormatter()
val S: TimeFormatter = TimeSecondFormatter()
fun Long.toSecond(): Long = this / 1000

fun EyeResult.prettyPrint(memoryFormatter: MemoryFormatter = B, timeFormatter: TimeFormatter = M) {
    println("%s Eye Result".format(title))
    println("Partial allocated memory: %s".format(memoryFormatter.format(partialMemoryUsageInByte)))
    println("Executed in: %s".format(timeFormatter.format(executionDurationInMs)))
}

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

fun Int.asKb(): Long {
    return (this * 1024.0).toLong()
}

fun Int.asMb(): Long {
    return (this * 1048576.0).toLong()
}

fun Int.asSecond(): Long {
    return (this * 1000.0).toLong()
}

class NooraEyeAssertionException(message: String): Throwable(message)
