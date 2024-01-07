package io.github.janbarari.nooraeye.memory

import io.github.janbarari.nooraeye.toMb

class MemoryMegabyteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sMb".format(value.toMb())
    }
}