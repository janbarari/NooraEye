package io.github.janbarari.nooraeye.memory

import io.github.janbarari.nooraeye.toKb

class MemoryKilobyteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sKb".format(value.toKb())
    }
}