package io.github.janbarari.functioneye.time

import io.github.janbarari.functioneye.toSecond

class TimeSecondFormatter : TimeFormatter {
    override fun format(value: Long): String {
        return "%ss".format(value.toSecond())
    }
}