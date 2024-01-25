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
    nooraEye("S0") {
        (0..100000000000).toList().reversed()
    }.prettyPrint(
        memoryFormatter = MemoryFormatters.mb,
        timeFormatter = TimeFormatters.millis
    )

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

private fun garbageCollectorMXBean() {
    val gcConsole = ConsolePrinter(35)
    gcConsole.printFirstLine()
    gcConsole.printLine("Garbage Collector Bean")
    gcConsole.printBreakLine()
    ManagementFactory.getGarbageCollectorMXBeans().forEach {
        gcConsole.printLine("Name", it.name)
        gcConsole.printLine("isValid", it.isValid.toString())
        gcConsole.printLine("Collection count", it.collectionCount.toString())
        gcConsole.printLine("Collection time", it.collectionTime.toString())
        gcConsole.printBreakLine('-')
    }
    gcConsole.printLastLine()
}

private fun memoryMXBean() {
    val gcConsole = ConsolePrinter(35)
    gcConsole.printFirstLine()
    gcConsole.printLine("Memory Bean")
    gcConsole.printBreakLine()
    ManagementFactory.getMemoryMXBean().heapMemoryUsage.also {
        gcConsole.printLine("Heap")
        gcConsole.printBreakLine('-')
        gcConsole.printLine("Init", MemoryFormatters.mb.format(it.init))
        gcConsole.printLine("Used", MemoryFormatters.mb.format(it.used))
        gcConsole.printLine("Committed", MemoryFormatters.mb.format(it.committed))
        gcConsole.printLine("Max", MemoryFormatters.mb.format(it.max))
        gcConsole.printBreakLine()
    }
    ManagementFactory.getMemoryMXBean().nonHeapMemoryUsage.also {
        gcConsole.printLine("Non Heap")
        gcConsole.printBreakLine('-')
        gcConsole.printLine("Init", MemoryFormatters.mb.format(it.init))
        gcConsole.printLine("Used", MemoryFormatters.mb.format(it.used))
        gcConsole.printLine("Committed", MemoryFormatters.mb.format(it.committed))
        gcConsole.printLine("Max", MemoryFormatters.mb.format(it.max))
        gcConsole.printBreakLine()
    }
    gcConsole.printLine("Pending Finalization", "${ManagementFactory.getMemoryMXBean().objectPendingFinalizationCount}")
    gcConsole.printLine("Is Verbose", ManagementFactory.getMemoryMXBean().isVerbose.toString())
    gcConsole.printLastLine()
}

private fun threadMXBean() {
    val gcConsole = ConsolePrinter(35)
    gcConsole.printFirstLine()
    gcConsole.printLine("Thread Bean")
    gcConsole.printBreakLine()
    ManagementFactory.getThreadMXBean().also {
        gcConsole.printLine("Threads", it.threadCount.toString())
        gcConsole.printLine("Started Threads", it.totalStartedThreadCount.toString())
        gcConsole.printLine("Current Thread CPU Time", it.currentThreadCpuTime.toString())
        gcConsole.printLine("Current Thread User Time", it.currentThreadUserTime.toString())
    }
    gcConsole.printLastLine()
}

private fun runtimeMXBean() {
    val gcConsole = ConsolePrinter(45)
    gcConsole.printFirstLine()
    gcConsole.printLine("Runtime Bean")
    gcConsole.printBreakLine()
    ManagementFactory.getRuntimeMXBean().also {
        gcConsole.printLine("Name", it.name)
        gcConsole.printLine("VM", it.vmName)
        gcConsole.printLine("VM Vendor", it.vmVendor)
        gcConsole.printLine("Spec", it.specName)
    }
    gcConsole.printLastLine()
}

private fun memoryPoolMXBean() {
    val gcConsole = ConsolePrinter(45)
    gcConsole.printFirstLine()
    gcConsole.printLine("Memory Pool Bean")
    gcConsole.printBreakLine()
    ManagementFactory.getMemoryPoolMXBeans().forEach {
        gcConsole.printLine("Name", it.name)
        gcConsole.printLine("Type", it.type.name)
        gcConsole.printLine("Used", MemoryFormatters.mb.format(it.usage.used))
        gcConsole.printBreakLine('-')
    }
    gcConsole.printLastLine()
}

private fun memoryManagerMXBean() {
    val gcConsole = ConsolePrinter(25)
    gcConsole.printFirstLine()
    gcConsole.printLine("Memory Manager Bean")
    gcConsole.printBreakLine()
    ManagementFactory.getMemoryManagerMXBeans().forEach {
        gcConsole.printLine(it.name)
    }
    gcConsole.printLastLine()
}













