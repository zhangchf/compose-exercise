package com.zcf.experiment

import org.junit.Assert
import org.junit.Test

class KotlinTest {

  @Test
  fun stringTemplateWithNull() {
    val countryCode: String? = null
    Assert.assertFalse("+86123456".startsWith("+$countryCode"))
  }

  @Test
  fun emptyStringSplit() {
    val splitResult = "".split(",")
    Assert.assertTrue(splitResult.isEmpty())
  }

  @Test
  fun isLetter() {
    Assert.assertTrue('.'.isLetter())
  }

  @Test
  fun whenStatement() {
    var x = 4
    var y = 3
    when {
      x == 5 -> println("zcf 5")
      y == 6 -> println("zcf 6")
    }
    var z = when {
      x == 5 -> println("zcf 5")
      y == 6 -> println("zcf 6")
      else -> {}
    }
  }
}
