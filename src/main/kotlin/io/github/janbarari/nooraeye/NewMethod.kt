package io.github.janbarari.nooraeye

import java.lang.management.ManagementFactory

fun getGcCount(): Long {
    var sum: Long = 0
    for (b in ManagementFactory.getGarbageCollectorMXBeans()) {
        val count = b.collectionCount
        if (count != -1L) {
            sum += count
        }
    }
    return sum
}

fun getReallyUsedMemory(): Long {
    val before = getGcCount()
    System.gc()
    while (getGcCount() == before);
    return getCurrentlyAllocatedMemory()
}

fun getCurrentlyAllocatedMemory(): Long {
    val runtime = Runtime.getRuntime()
    return runtime.totalMemory() - runtime.freeMemory()
}

fun main(args: Array<String>) {


    functionEye("Test #1") {
        // Reverse a 100 million numbers
        (0 until 100_000_000).toList().reversed()
    }.prettyPrint(KB)


}
