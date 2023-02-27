package com.zcf.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test

/**
 * Multicasting for RxJava: https://ithelp.ithome.com.tw/articles/10266619
 */
class CacheAndPublishTest {

    @Test
    fun `For a cached Single, the origin observable will start only once`() {
        var count = 0
        val testSingle = Single.fromCallable {
            println("$count")
            count++
        }
            .map {
                val result = it * 10
                println("$result")
                result
            }
            .cache()

        testSingle.test().assertValue(0)
        testSingle.test().assertValue(0)
        testSingle.test().assertValue(0)
    }

    @Test
    fun `Dispose a cached observable doesn't dispose its origin observable`() {
        val endSignal = PublishSubject.create<Unit>()
        val originObservable = Observable.create<Int> { emitter ->
            var count = 0
            while (!emitter.isDisposed) {
                count += 1
                println("zcf count: $count")
                emitter.onNext(count)
                Thread.sleep(1000)
            }
        }
            .subscribeOn(Schedulers.computation())
            .takeUntil(endSignal)
            .cache()

        println("zcf start test")
        val disposable = originObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe({ println("onNext: $it") }, { println("onError: $it") }, { println("onComplete") })

        Thread.sleep(3000)
        disposable.dispose()
        println("zcf disposed")
        Thread.sleep(3000)
        endSignal.onNext(Unit)
        println("zcf endSignal")
        Thread.sleep(5000)
        println("zcf complete")
    }

    @Test
    fun `Publish+Refcount share an observable, wait for required subscribers, and dispose when all observers have unsubscribed`() {
        val originObservable = Observable.wrap<Int> { observer ->
            for (count in 0..10) {
                Thread.sleep(1000)
                println("zcf count: $count")
                observer.onNext(count)
            }
        }
            .subscribeOn(Schedulers.computation())
            .publish()
            .refCount(2)

        println("zcf start test, ${System.currentTimeMillis()}")
        Thread.sleep(3000)
        println("zcf start test2, ${System.currentTimeMillis()}")
        val disposable1 = originObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe({ println("onNext: $it") }, { println("onError: $it") }, { println("onComplete") })
        val disposable2 = originObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe({ println("onNext2: $it") }, { println("onError2: $it") }, { println("onComplete2") })

        Thread.sleep(3000)
        disposable1.dispose()
        Thread.sleep(3000)
        disposable2.dispose()
        Thread.sleep(5000)
        println("zcf disposed")
    }

    @Test
    fun `Publish+Connect share an observable, wait for connect, and dispose when all the connected disposable is disposed`() {
        val originObservable = Observable.wrap<Int> { observer ->
            for (count in 0..10) {
                Thread.sleep(1000)
                println("zcf count: $count")
                observer.onNext(count)
            }
        }
            .subscribeOn(Schedulers.computation())
            .publish()

        println("zcf start test, ${System.currentTimeMillis()}")
        val disposable1 = originObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe({ println("onNext: $it") }, { println("onError: $it") }, { println("onComplete") })
        val disposable2 = originObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe({ println("onNext2: $it") }, { println("onError2: $it") }, { println("onComplete2") })


        Thread.sleep(3000)
        println("zcf start test2, ${System.currentTimeMillis()}")
        val originDisposable = originObservable.connect()
        println("zcf start test3, ${System.currentTimeMillis()}")
        Thread.sleep(3000)
        disposable1.dispose()
        Thread.sleep(3000)
        disposable2.dispose()
        Thread.sleep(3000)
        originDisposable.dispose()
        Thread.sleep(5000)
        println("zcf disposed")
    }
}