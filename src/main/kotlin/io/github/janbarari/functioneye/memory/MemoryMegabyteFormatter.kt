package io.github.janbarari.functioneye.memory

import io.github.janbarari.functioneye.toMb

class MemoryMegabyteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sMb".format(value.toMb())
    }
}