package com.zcf.rxjava

import io.reactivex.rxjava3.core.Single
import org.junit.Test

class LazySingleTest {

    @Test
    fun `non-lazy Single`() {
        var count = 0
        val testSingle = Single.fromCallable {
            count++
            println("$count")
        }.map { count * 10 }

        testSingle.test().assertValue(10)
        testSingle.test().assertValue(20)
        testSingle.test().assertValue(30)
    }

    @Test
    fun `lazy Single`() {
        var count = 0
        val testSingle by lazy {
            Single.fromCallable {
                count++
                println("$count")
            }.map { count * 10 }
        }

        testSingle.test().assertValue(10)
        testSingle.test().assertValue(20)
    }

    @Test
    fun `cached Single`() {
        var count = 0
        val testSingle = Single.fromCallable {
            count++
            println("$count")
        }
            .map { count * 10 }
            .cache()

        testSingle.test().assertValue(10)
        testSingle.test().assertValue(10)
        testSingle.test().assertValue(10)
    }
}