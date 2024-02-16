package io.github.janbarari.nooraeye

class EyeProgress(private val title: String) {
    var isEyeProgressInitiated = false
    private var progress: Int = 0

    private fun ensureInitiated() {
        if (!isEyeProgressInitiated) {
            isEyeProgressInitiated = true
            println()
        }
    }
    fun setProgress(progress: Int) {
        ensureInitiated()
        if (progress == this.progress) return
        this.progress = progress
        printProgress()
    }

    fun incProgress(value: Int) {
        ensureInitiated()
        progress += value
        if (progress > 100) progress = 100
        printProgress()
    }

    private fun printProgress() {
        print("\r  NooraEye(${title}) [")
        val progressChars = (progress.toDouble() / 100 * 24).toInt()
        repeat(progressChars) { print("◼") }
        repeat(24 - progressChars) { print(" ") }
        print("] $progress%")
    }
}
