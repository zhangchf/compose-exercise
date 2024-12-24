package com.zcf.experiment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.Executors

/**
 * Exploring Coroutines through Output Questions:
 * https://proandroiddev.com/exploring-kotlin-coroutines-through-output-questions-aa0760383569
 */
class CoroutineTest {

    val singleThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    suspend fun doLongRunningTaskOne(): Int {
        delay(2400)
        println("Task one is getting executed")
        return 33
    }

    suspend fun doLongRunningTaskTwo(): Int {
        delay(1500)
        println("Task two is getting executed")
        return 6
    }

    /**
     * I'm outside coroutineScope
     * I've launched both coroutines
     * Task two is getting executed
     * Task one is getting executed
     * Completed my execution
     */
    @Test
    fun `coroutine launch in the same context, waits for its completion`() {
        runBlocking {
            launch {
                launch { doLongRunningTaskOne() }
                launch { doLongRunningTaskTwo() }
                println("I've launched both coroutines")
            }
            println("I'm outside coroutineScope")
        }
        println("Completed my execution")
    }

    /**
     * I'm outside coroutineScope
     * I've launched both coroutines
     * Completed my execution
     */
    @Test
    fun `coroutine launch in another thread, doesn't wait for its completion`() {
        runBlocking {
            CoroutineScope(Dispatchers.Default).launch {
                launch { doLongRunningTaskOne() }
                launch { doLongRunningTaskTwo() }
                println("I've launched both coroutines")
            }
            println("I'm outside coroutineScope")
        }
        println("Completed my execution")
    }

    /**
     * I'm outside coroutineScope
     * I've launched both coroutines
     * Completed my execution
     */
    @Test
    fun `coroutine launch in another thread, join doesn't wait for its completion`() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                CoroutineScope(Dispatchers.IO).launch {
                    launch { doLongRunningTaskOne() }
                    launch { doLongRunningTaskTwo() }
                    println("I've launched both coroutines")
                }
                println("I'm outside coroutineScope")
            }.join()
            println("Completed my execution")
        }
    }


    /**
     * I've launched both coroutines of scope 1
     * I'm outside coroutineScope
     * Completed my execution
     */
    @Test
    fun `coroutine launch in another thread, join doesn't waits for its completion`() {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
//                launch { doLongRunningTaskOne() }
//                launch { doLongRunningTaskTwo() }
                println("I've launched both coroutines of scope 1")

                CoroutineScope(singleThreadDispatcher).launch {
                    launch { doLongRunningTaskOne() }
                    launch { doLongRunningTaskTwo() }
                    println("I've launched both coroutines of scope 2")
                }
            }.join()
            println("I'm outside coroutineScope")
        }
        println("Completed my execution")
    }
}