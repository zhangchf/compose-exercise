package com.zcf.composeexercise

import android.icu.math.BigDecimal
import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.icu.text.CompactDecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.icu.util.ULocale
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.math.RoundingMode
import java.util.*

class CompactDecimalFormatTest {
    private val tag = "zcf1"

    val number = 123.12

    @Test
    fun whenGetInstanceAgain_willGetDifferentInstances() {
        val currencyFormatter = CompactDecimalFormat.getInstance(ULocale.CHINA, CompactDecimalFormat.CompactStyle.LONG)
        currencyFormatter.currency = Currency.getInstance("EUR")
        val numberFormatter = CompactDecimalFormat.getInstance(ULocale.CHINA, CompactDecimalFormat.CompactStyle.LONG)

        println("$tag, currencyFormatter: $currencyFormatter, numberFormatter: $numberFormatter ")
        assertNotEquals(currencyFormatter, numberFormatter)

        var result = currencyFormatter.format(number)
        println("$tag currencyFormatter, result: $result")

        result = numberFormatter.format(number)
        println("$tag numberFormatter, result: $result")
    }

    @Test
    fun addAndRemoveCurrency_willWorkForCurrencyAndNumber() {
        val formatter = CompactDecimalFormat.getInstance(ULocale.CHINA, CompactDecimalFormat.CompactStyle.LONG)
        formatter.currency = Currency.getInstance("EUR")
        formatter.roundingMode = BigDecimal.ROUND_HALF_UP

        val result1 = formatter.format(number)
        println("$tag result: $result1")

        formatter.currency = null
        val result2 = formatter.format(number)
        println("$tag result: $result2")

        formatter.currency = Currency.getInstance("EUR")
        val result3 = formatter.format(number)
        println("$tag result: $result3")

        assertNotEquals(result1, result2)
        assertEquals(result1, result3)
    }

    @Test
    fun nullCompactStyle_notWorkForCurrency() {
        val formatter = CompactDecimalFormat.getInstance(ULocale.CHINA)
        formatter.currency = Currency.getInstance("EUR")

        val result1 = formatter.format(number)
        println("$tag result: $result1")

        formatter.currency = null
        val result2 = formatter.format(number)
        println("$tag result: $result2")

        assertEquals(result1, result2)
    }

    @Test
    fun longAndShortCompatStyle_looksNoDifferentForBasicUsage() {
        val formatter = CompactDecimalFormat.getInstance(ULocale.CHINA, CompactDecimalFormat.CompactStyle.LONG)
        formatter.currency = Currency.getInstance("EUR")
        formatter.roundingMode = BigDecimal.ROUND_HALF_UP

        val result1 = formatter.format(number)
        println("$tag result1: $result1")

        formatter.currency = null
        val result2 = formatter.format(number)
        println("$tag result2: $result2")

        val formatterShortStyle = CompactDecimalFormat.getInstance(ULocale.CHINA, CompactDecimalFormat.CompactStyle.SHORT)
        formatterShortStyle.currency = Currency.getInstance("EUR")
        formatterShortStyle.roundingMode = BigDecimal.ROUND_HALF_UP

        val shortResult1 = formatterShortStyle.format(number)
        println("$tag shortResult1: $shortResult1")

        formatterShortStyle.currency = null
        val shortResult2 = formatterShortStyle.format(number)
        println("$tag shortResult2: $shortResult2")

        assertEquals(result1, shortResult1)
        assertEquals(result2, shortResult2)
    }
}