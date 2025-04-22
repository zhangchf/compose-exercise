package com.zcf.composeexercise

import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.util.Currency
import android.icu.util.ULocale
import org.junit.Test
import java.util.*

class NumberFormatterTest {

  @Test
  fun currencyFormatTest() {
    val result = NumberFormatter.with()
      .notation(Notation.compactShort())
      .unit(Currency.getInstance("EUR"))
      .precision(Precision.maxSignificantDigits(3))
      .locale(ULocale.CHINA)
      .format(1234000000)
      .toString()

    println("zcf result: $result")
  }
}
