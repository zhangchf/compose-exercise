package com.zcf.rxjava

import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Test

class ObservableTest {

  @Test
  fun firstOrError() {
    val source1 = BehaviorSubject.create<Int>()

    val observer = source1.firstOrError().test()

    source1.onNext(1)

    source1.onNext(2)
    source1.onNext(3)

    observer.assertValues(1)
    observer.assertComplete()
  }
}
