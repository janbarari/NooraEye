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

import io.github.janbarari.nooraeye.memory.MemoryFormatter
import io.github.janbarari.nooraeye.time.TimeFormatter

data class EyeResult(
    val title: String,
    val memoryUsageInByte: Long = 0,
    val executionDurationInMs: Long = 0,
    val isMemoryAccurate: Boolean = false,
    val gcTriggerCount: Long = 0,
    val isRanOutOfMemory: Boolean = false,
    val maximumReachedHeapMemoryInByte: Long = 0
)

fun EyeResult.prettyPrint(
    memoryFormatter: MemoryFormatter = MemoryFormatters.byte,
    timeFormatter: TimeFormatter = TimeFormatters.millis
) {
    val title = "%s Eye Result".format(title)
    if (!isRanOutOfMemory) {
        val note = "If GC happens during the execution of the process, memory precision will drop."
        var consoleLength = 16 + title.length
        consoleLength += if (memoryFormatter.format(memoryUsageInByte).length > timeFormatter.format(
                executionDurationInMs
            ).length
        ) {
            memoryFormatter.format(memoryUsageInByte).length
        } else {
            timeFormatter.format(executionDurationInMs).length
        }
        ConsolePrinter(consoleLength).run {
            printFirstLine()
            printLine(title)
            printBreakLine()
            printLine("Memory usage", memoryFormatter.format(memoryUsageInByte))
            printLine("Memory precision", if (isMemoryAccurate) "Accurate" else "Approximate")
            printLine("GC Triggers", gcTriggerCount.toString())
            printLine("Executed in", timeFormatter.format(executionDurationInMs))
            printBreakLine('-')
            splitText(note).forEach {
                printLine(it)
            }
            printLastLine()
        }
    } else {
        var consoleLength = 8 + title.length
        consoleLength += MemoryFormatters.mb.format(maximumReachedHeapMemoryInByte).length
        ConsolePrinter(consoleLength).run {
            printFirstLine()
            printLine(title)
            printBreakLine()
            printLine("Ran out in", MemoryFormatters.mb.format(maximumReachedHeapMemoryInByte))
            printLastLine()
        }
    }
}
