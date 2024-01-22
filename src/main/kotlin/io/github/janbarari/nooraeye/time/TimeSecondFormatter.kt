package io.github.janbarari.nooraeye.time

import io.github.janbarari.nooraeye.toSecond

class TimeSecondFormatter : TimeFormatter {
    override fun format(value: Long): String {
        return "%ss".format(value.toSecond())
    }
}