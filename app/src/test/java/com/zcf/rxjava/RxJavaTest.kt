package com.zcf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test
import java.lang.IllegalStateException

class RxJavaTest {
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
    fun `merge and mergeArray has no much difference`() {

        val source1 = PublishSubject.create<Int>()
        val source2 = PublishSubject.create<String>()

        val observer1 = Observable.merge(source1, source2)
            .test()

        val observer2 = Observable.mergeArray(source1, source2)
            .test()

        source1.onNext(1)
        source2.onNext("a")
        source1.onNext(2)
        source2.onNext("b")

        observer1.assertValues(1, "a", 2, "b")
        observer2.assertValues(1, "a", 2, "b")

        source1.onNext(3)
        source2.onNext("c")

        observer1.assertValues(1, "a", 2, "b", 3, "c")
        observer2.assertValues(1, "a", 2, "b", 3, "c")
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
}