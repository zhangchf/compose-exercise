package com.zcf.experiment

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FlowTest {

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `flow operate sequentially`() = runTest {
    println("TestScope begin")
    var mapCount = 0
    var firstOrNullCount = 0

    flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
      .map {
        mapCount++
        println("map: $it")
        it
      }
      .firstOrNull {
        firstOrNullCount++
        println("firstOrNull: $it")
        it == 3
      }

    Assert.assertEquals(mapCount, 3)
    Assert.assertEquals(firstOrNullCount, 3)
    println("TestScope end")
  }
}
