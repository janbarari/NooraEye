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

fun EyeResult.prettyPrint(memoryFormatter: MemoryFormatter = B, timeFormatter: TimeFormatter = M) {
    println("    %s Eye Result".format(title))
    println("    Partial allocated memory: %s".format(memoryFormatter.format(partialMemoryUsageInByte)))
    println("    Executed in: %s".format(timeFormatter.format(executionDurationInMs)))
}
