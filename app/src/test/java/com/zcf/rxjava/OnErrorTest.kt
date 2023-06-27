package com.zcf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test
import java.lang.IllegalStateException

class OnErrorTest {
    @Test
    fun `onErrorReturnItem will complete merged observable`() {

        val source1 = PublishSubject.create<Int>()
        val source2 = PublishSubject.create<String>()

        val observer = Observable.merge(source1, source2)
            .onErrorReturnItem("x")
            .test()

        observer.assertNotComplete()

        assert(observer.hasSubscription())

        source1.onNext(1)
        source2.onNext("a")

        observer.assertValues(1, "a")

        source1.onError(IllegalStateException("test error"))
        source2.onNext("b")
        observer.assertValues(1, "a", "x")
        observer.assertComplete()
    }

    @Test
    fun `onErrorReturnItem will complete observable`() {

        val source1 = PublishSubject.create<Int>()
        val observer = source1
            .onErrorReturnItem(0)
            .test()

        observer.assertNotComplete()

        assert(observer.hasSubscription())

        source1.onNext(1)

        observer.assertValues(1)

        source1.onError(IllegalStateException("test error"))
        source1.onNext(2)

        observer.assertValues(1, 0)
        observer.assertComplete()
    }

    @Test
    fun `onErrorReturnItem works for inside flatMap`() {

        val source1 = PublishSubject.create<Int>()
        val observer = Observable.just(1)
            .flatMap {
                source1
            }
            .onErrorReturnItem(1000)
            .test()

        observer.assertNotComplete()

        assert(observer.hasSubscription())

        source1.onNext(1)

        observer.assertValues(1)

        source1.onError(IllegalStateException("test error"))
        source1.onNext(2)

        observer.assertValues(1, 1000)
        observer.assertComplete()
    }
}