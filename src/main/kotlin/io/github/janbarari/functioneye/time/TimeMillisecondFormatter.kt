package io.github.janbarari.functioneye.time

class TimeMillisecondFormatter : TimeFormatter {
    override fun format(value: Long): String {
        return "%sms".format(value)
    }
}