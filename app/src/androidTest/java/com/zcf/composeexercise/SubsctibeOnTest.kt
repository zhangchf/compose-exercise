package com.zcf.composeexercise

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Assert
import org.junit.Test

class SubsctibeOnTest {

    /**
     * subscribeOn specifies on which thread the observable will run.
     * But a flatMap observable will run on its own thread if it specifies one.
     * If there is no need to switch thread, it will run on the current thread.
     *
     * Logs:
     * observable1, thread: Thread[RxCachedThreadScheduler-1,5,main]
     * observable1 map, thread : Thread[RxCachedThreadScheduler-1,5,main]
     * observable1 flatMap, thread: Thread[RxCachedThreadScheduler-1,5,main]
     * observable2: Thread[RxCachedThreadScheduler-2,5,main]
     * observable2 map, thread : Thread[RxCachedThreadScheduler-2,5,main]
     * subscribe, thread: Thread[RxCachedThreadScheduler-2,5,main]
     * result: 2
     */
    @Test
    fun subscribeOn_specifyWhichThreadToRun() {
        val observable1 = Observable.fromCallable {
            println("zcf observable1, thread: ${Thread.currentThread()}")
            1
        }
            .map {
                println("zcf observable1 map, thread : ${Thread.currentThread()}")
                it
            }
            .subscribeOn(Schedulers.io())

        val observable2 = Observable.fromCallable {
            println("zcf observable2: ${Thread.currentThread()}")
            2
        }
            .map {
                println("zcf observable2 map, thread : ${Thread.currentThread()}")
                it
            }
            .subscribeOn(Schedulers.io())

        val observable3 = observable1.flatMap { result1 ->
            println("zcf observable1 flatMap, thread: ${Thread.currentThread()}")
            observable2
        }
            .subscribeOn(Schedulers.newThread())
            .subscribe {
                println("zcf subscribe, thread: ${Thread.currentThread()}")
                println("zcf result: $it")
            }

        Thread.sleep(1000)
        Assert.assertTrue(observable3.isDisposed)
    }

    /**
     * observeOn will change the thread the execute the subscribe method.
     *
     * Logs:
     * observable1, thread: Thread[RxCachedThreadScheduler-1,5,main]
     * observable1 map, thread : Thread[RxCachedThreadScheduler-1,5,main]
     * observable1 flatMap, thread: Thread[RxCachedThreadScheduler-1,5,main]
     * observable2: Thread[RxCachedThreadScheduler-2,5,main]
     * observable2 map, thread : Thread[RxCachedThreadScheduler-2,5,main]
     * subscribe, thread: Thread[main,5,main]
     * result: 2
     */
    @Test
    fun observeOn_changesThreadToSubscribe() {
        val observable1 = Observable.fromCallable {
            println("zcf observable1, thread: ${Thread.currentThread()}")
            1
        }
            .map {
                println("zcf observable1 map, thread : ${Thread.currentThread()}")
                it
            }
            .subscribeOn(Schedulers.io())

        val observable2 = Observable.fromCallable {
            println("zcf observable2: ${Thread.currentThread()}")
            2
        }
            .map {
                println("zcf observable2 map, thread : ${Thread.currentThread()}")
                it
            }
            .subscribeOn(Schedulers.io())

        val observable3 = observable1.flatMap { result1 ->
            println("zcf observable1 flatMap, thread: ${Thread.currentThread()}")
            observable2
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println("zcf subscribe, thread: ${Thread.currentThread()}")
                println("result: $it")
            }

        Thread.sleep(1000)
        Assert.assertTrue(observable3.isDisposed)
    }

    /**
     * Logs:
     * observable1, thread: Thread[RxNewThreadScheduler-1,5,main]
     * observable1 map, thread : Thread[RxNewThreadScheduler-1,5,main]
     * observable1 flatMap, thread: Thread[RxNewThreadScheduler-1,5,main]
     * observable2: Thread[RxCachedThreadScheduler-1,5,main]
     * observable2 map, thread : Thread[RxCachedThreadScheduler-1,5,main]
     * subscribe, thread: Thread[RxNewThreadScheduler-2,5,main]
     * result: 2
     */
    @Test
    fun subscribeOn_specifyWhichThreadToRun2() {
        val observable1 = Observable.fromCallable {
            println("zcf observable1, thread: ${Thread.currentThread()}")
            1
        }
            .map {
                println("zcf observable1 map, thread : ${Thread.currentThread()}")
                it
            }
            .subscribeOn(Schedulers.newThread())

        val observable2 = Observable.fromCallable {
            println("zcf observable2: ${Thread.currentThread()}")
            2
        }
            .map {
                println("zcf observable2 map, thread : ${Thread.currentThread()}")
                it
            }
            .subscribeOn(Schedulers.io())

        val observable3 = observable1.flatMap { result1 ->
            println("zcf observable1 flatMap, thread: ${Thread.currentThread()}")
            observable2
        }
            .observeOn(Schedulers.newThread())
            .subscribe {
                println("zcf subscribe, thread: ${Thread.currentThread()}")
                println("zcf result: $it")
            }

        Thread.sleep(1000)
        Assert.assertTrue(observable3.isDisposed)
    }
}