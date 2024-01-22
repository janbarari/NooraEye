package io.github.janbarari.nooraeye.memory

class MemoryByteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sB".format(value)
    }
}