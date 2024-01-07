package io.github.janbarari.nooraeye

class Lock {

    var isLocked = false

    fun lock() = synchronized(this) {
        isLocked = true
    }

    fun unlock() = synchronized(this) {
        isLocked = false
    }
}

val lock = Lock()
