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

import io.github.janbarari.nooraeye.assertion.assertNooraEye
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class FunctionEyeTest {

    @Test
    fun `check functionEye function works correctly`() {

    }

    @Test
    fun `check assertFunctionEye passes when execution doesn't exceed the thresholds`() {
        assertDoesNotThrow {
            assertNooraEye("S01", 10.mbToByte(), 1.secondToMillis()) {
                Thread.sleep(500)
            }
        }
    }

    @Test
    fun `check assertFunctionEye throws exception when execution does exceed the thresholds`() {

    }

}
