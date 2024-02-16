package io.github.janbarari

import io.github.janbarari.nooraeye.MemoryFormatters
import io.github.janbarari.nooraeye.nooraEye
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() {
    val dataset = (0..100000).toSet()

    val without = nooraEye("Test #0629") {
        runBlocking {
            dataset.filter {
                delayNanos(70000)
                setProgress(it / 1000)
                it % 2 == 0
            }
        }
    }
    without.prettyPrint(
        memoryFormatter = MemoryFormatters.kb
    )

//    val with = nooraEye("With") {
//        runBlocking {
//            dataset.parallel(parallelForEvery = 5) {
//                filter {
//                    delay(1)
//                    it % 2 == 0
//                }
//            }
//        }
//    }
//    with.prettyPrint(
//        memoryFormatter = MemoryFormatters.kb
//    )

}

fun <T> Iterable<T>.parallel(parallelForEvery: Int = 1, block: suspend Iterable<T>.() -> List<T>): List<T> {
    return runBlocking {
        val deferredJobs = mutableListOf<Deferred<Any>>()
        val jobResults: MutableList<Pair<Int, List<T>>> = mutableListOf()
        this@parallel.chunked(parallelForEvery).forEachIndexed { index, chunk ->
            val deferredJob = async {
                jobResults.add(index to block(chunk))
            }
            deferredJobs.add(deferredJob)
        }
        deferredJobs.map { it.await() }
        jobResults.sortedBy { it.first }.flatMap { it.second }
    }
}
