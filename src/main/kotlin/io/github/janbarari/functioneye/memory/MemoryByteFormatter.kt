package io.github.janbarari.functioneye.memory

class MemoryByteFormatter : MemoryFormatter {
    override fun format(value: Long): String {
        return "%sB".format(value)
    }
}