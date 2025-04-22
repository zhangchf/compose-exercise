package com.zcf.composeexercise

import android.icu.util.Currency
import org.junit.Test
import java.util.*

class CurrencyTest {

  @Test
  fun currencyTest() {
    val localLaunguage = Locale.getDefault().language
    val locale = Locale(Locale.getDefault().language, "FR")
    val currency = Currency.getInstance(locale)
    println("zcf $locale, $currency")
  }
}
