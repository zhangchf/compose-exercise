package com.zcf.experiment

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class ModelTest {

    @Test
    fun model_testEquals() {
        val model1 = Model("test", byteArrayOf(1, 2, 3))
        val model2 = Model("test", byteArrayOf(1, 2, 3))

        Assert.assertEquals(model1, model2)
    }

    @Test
    fun map_testEquals() {
        val model1 = mapOf("abc" to 100, "def" to 200)
        val model2 = mapOf("abc" to 100, "def" to 200)

        Assert.assertEquals(model1, model2)
    }

    @Test
    fun map_testNotEquals() {
        val model1 = mapOf("abc" to 100, "def" to 300)
        val model2 = mapOf("abc" to 100, "def" to 200)

        Assert.assertNotEquals(model1, model2)
    }
}