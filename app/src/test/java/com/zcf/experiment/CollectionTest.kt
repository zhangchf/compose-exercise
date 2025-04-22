package com.zcf.experiment

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.junit.Assert
import org.junit.Test

class CollectionTest {

  @Test
  fun `map doesn't operate sequentially`() {
    var mapCount = 0
    var firstOrNullCount = 0
    (1..10)
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

    Assert.assertEquals(mapCount, 10)
    Assert.assertEquals(firstOrNullCount, 3)
  }

  @Test
  fun `forEach doesn't operate sequentially`() {
    var forEachCount = 0
    (1..10)
      .forEach {
        forEachCount++
        println("forEach: $it")
        if (it == 3) return@forEach
      }

    Assert.assertEquals(forEachCount, 10)
  }
}
