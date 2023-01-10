package com.zcf.composeexercise

import org.junit.Assert
import org.junit.Test
import java.util.MissingFormatArgumentException

class StringFormatTest {
    @Test
    fun stringFormatWithExactArgs() {
        val result = String.format("I'm a string with placeholder %s.", "abcd")
        Assert.assertEquals(result, "I'm a string with placeholder abcd.")
    }

    @Test
    fun stringFormatWithRedundantArgs() {
        val result = String.format("I'm a string with placeholder.", "abcd")
        Assert.assertEquals(result, "I'm a string with placeholder.")
    }

    @Test(expected = MissingFormatArgumentException::class)
    fun stringFormatWithLessArgs() {
        val result = String.format("I'm a string with placeholder %s")
        Assert.assertEquals(result, "I'm a string with placeholder.")
    }
}