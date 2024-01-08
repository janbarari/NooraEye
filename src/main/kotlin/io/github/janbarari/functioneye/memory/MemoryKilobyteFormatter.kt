package io.github.janbarari.functioneye.memory

import io.github.janbarari.functioneye.toKb

class MemoryKilobyteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sKb".format(value.toKb())
    }
}