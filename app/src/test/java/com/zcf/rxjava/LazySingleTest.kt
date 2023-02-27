package com.zcf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test

class LazySingleTest {

    @Test
    fun `For a non-lazy cold Single, the origin observable will start every time it's subscribed`() {
        var count = 0
        val testSingle = Single.fromCallable {
            println("$count")
            count++
        }.map {
            val result = it * 10
            println("$result")
            result
        }

        testSingle.test().assertValue(0)
        testSingle.test().assertValue(10)
        testSingle.test().assertValue(20)
    }

    @Test
    fun `For a lazy created Single, the origin observable still starts each time it's subscribed`() {
        var count = 0
        val testSingle by lazy {
            Single.fromCallable {
                println("$count")
                count++
            }.map {
                val result = it * 10
                println("$result")
                result
            }
        }

        testSingle.test().assertValue(0)
        testSingle.test().assertValue(10)
        testSingle.test().assertValue(20)
    }
}