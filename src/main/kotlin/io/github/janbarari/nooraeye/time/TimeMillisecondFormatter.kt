package io.github.janbarari.nooraeye.time

class TimeMillisecondFormatter : TimeFormatter {
    override fun format(value: Long): String {
        return "%sms".format(value)
    }
}