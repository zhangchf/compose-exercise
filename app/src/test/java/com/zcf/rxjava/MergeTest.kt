package com.zcf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test

class MergeTest {

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

}