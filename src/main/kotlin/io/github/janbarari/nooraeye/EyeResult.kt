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
    val partialMemoryUsageInByte: Long,
    val executionDurationInMs: Long
)

fun EyeResult.prettyPrint(
    memoryFormatter: MemoryFormatter = MemoryFormatters.byte,
    timeFormatter: TimeFormatter = TimeFormatters.millis
) {
    val title = "%s Eye Result".format(title)
    var consoleLength = 6 + title.length
    consoleLength += if (memoryFormatter.format(partialMemoryUsageInByte).length > timeFormatter.format(executionDurationInMs).length) {
        memoryFormatter.format(partialMemoryUsageInByte).length
    } else {
        timeFormatter.format(executionDurationInMs).length
    }
    ConsolePrinter(consoleLength).run {
        printFirstLine()
        printLine(title)
        printBreakLine()
        printLine("Memory usage", memoryFormatter.format(partialMemoryUsageInByte))
        printLine("Executed in", timeFormatter.format(executionDurationInMs))
        printLastLine()
    }
}
