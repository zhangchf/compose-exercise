package com.zcf.experiment

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.lang.Exception

fun dataFlow() = flow<Int> {
  for (i in 1..10) {
    if (i == 3) error("exception at $i") else emit(i)
  }
}

fun <T> Flow<T>.handleErrors() = flow<T> {
  try {
    collect { emit(it) }
  } catch (e: Exception) {
    println("caught exception: $e")
  }
}

fun main() = runBlocking {
  println("Hello")

  coroutineScope {
    dataFlow()
      .onEach { delay(200) }
      .catch { e -> println("exception: $e") }
//            .collect { error("throws error") }
      .collect { println(it) }
  }
}
