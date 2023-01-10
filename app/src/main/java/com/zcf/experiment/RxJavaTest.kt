package com.zcf.experiment

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

fun main()  {
    val source1 = Observable.just(1, 2, 3)
    val source2 = Observable.just(4).delay(1, TimeUnit.SECONDS)

    val observer = Observable.merge(
        Observable.just(4).delay(3, TimeUnit.SECONDS),
        Observable.just(1, 2, 3)
    ).observeOn(AndroidSchedulers.mainThread())

    observer.subscribe {
        println("zcf $it")
    }

    Thread.sleep(5000)
}