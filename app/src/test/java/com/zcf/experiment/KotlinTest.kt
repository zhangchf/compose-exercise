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


}