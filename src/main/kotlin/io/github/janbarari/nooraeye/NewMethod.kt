package io.github.janbarari.nooraeye

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    runBlocking {
        launch {
            nooraEye("A") {
                runBlocking { delay(1000) }
            }.prettyPrint()
        }
        launch {
            //delay(1100)
            nooraEye("B") {
                runBlocking { delay(1000) }
            }.prettyPrint()
        }
    }
}
//    CoroutineScope(Dispatchers.Default).launch {
//        nooraEye("b") {
//            runBlocking { delay(1000) }
//        }.prettyPrint()
//    }
