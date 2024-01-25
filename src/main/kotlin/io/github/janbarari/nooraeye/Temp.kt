package io.github.janbarari.nooraeye

import java.io.File
import java.lang.management.ManagementFactory

const val FILE_NAME = "test.txt"
const val LINES_COUNT = 2_000_000

fun createTestFile() {
    val repetitionCount = LINES_COUNT
    val repeatedString = ('A'.toString() + "\n").repeat(repetitionCount)
    val file = File(FILE_NAME)
    file.bufferedWriter().use { writer ->
        writer.write(repeatedString)
    }
    val fileSizeInBytes = file.length()
    val fileSizeInKb = fileSizeInBytes / 1024
    ConsolePrinter(36).apply {
        printFirstLine()
        printLine("File Size: ${fileSizeInKb}kb with $LINES_COUNT lines")
        printLastLine()
    }
}

fun deleteTestFile() {
    File(FILE_NAME).delete()
}

fun main() {
//    nooraEye("S0") {
//        (0..100000000000).toList().reversed()
//    }.prettyPrint(
//        memoryFormatter = MemoryFormatters.mb,
//        timeFormatter = TimeFormatters.millis
//    )

    createTestFile()

    val s1 = nooraEye("S1") {
        val bufferedReader = File(FILE_NAME).bufferedReader()
        var count = 0
        while (true) {
            bufferedReader.readLine() ?: break
            count++
        }
        bufferedReader.close()
        if (count != LINES_COUNT) throw Exception("Wrong Result")
    }
    s1.prettyPrint(
        memoryFormatter = MemoryFormatters.mb,
        timeFormatter = TimeFormatters.millis
    )

    val s2 = nooraEye("S2") {
        val count = File(FILE_NAME).readLines().count()
        if (count != LINES_COUNT) throw Exception("Wrong Result")
    }
    s2.prettyPrint(
        memoryFormatter = MemoryFormatters.mb,
        timeFormatter = TimeFormatters.millis
    )

    deleteTestFile()

    printComparison(s2, s1)
}
