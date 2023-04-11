package com.zcf.experiment.coroutine

import kotlinx.coroutines.*

val rootExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    println("rootExceptionHandler: $coroutineContext, $throwable")
}

@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking(rootExceptionHandler) {
//    try {
//        val job = GlobalScope.launch { // root coroutine with launch
//            println("Throwing exception from launch")
//            throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
//        }
//        println("Before job join")
//        job.join()
//        println("Joined failed job")
//    } catch (e: java.lang.Exception) {
//        println("Caught IndexOutOfBoundsException")
//    }

    try {
        supervisorScope {
            val child = launch {
                try {
                    println("The child is sleeping")
                    delay(Long.MAX_VALUE)
                } finally {
                    println("The child is cancelled")
                }
            }
            println("Before yield")
            // Give our child a chance to execute and print using yield
            yield()
            println("Throwing an exception from the scope")
            throw AssertionError()
        }
    } catch(e: AssertionError) {
        println("Caught an assertion error")
    }

//    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        println("exceptionHandler: $coroutineContext, $throwable")
//    }
//    val job = CoroutineScope(exceptionHandler).launch { // root coroutine with launch
//        println("Throwing exception from launch")
//        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
//    }
//    println("Before job join")
//    job.join()
//    println("After job join")
    val deferred = GlobalScope.async {
        delay(3000)// root coroutine with async
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }
    try {
        deferred.await()
        println("Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}