package com.zcf.rxjava

import io.reactivex.rxjava3.core.Single
import org.junit.Test

class ZipTest {

  @Test
  fun testZipAndFlatMap() {
    val result = Single.just(listOf(1, 2, 3))
      .flatMap { idList ->
        val idMap = idList.map { id ->
          Single.fromCallable { listOf(id, id) }
        }

        Single.zip(idMap) { idResult ->
          idResult.fold(mutableListOf<Int>()) { acc, item ->
            (item as List<*>).forEach { acc.add(it as Int) }
            acc
          }.toList()
        }
      }

    result.test().assertValue(listOf(1, 1, 2, 2, 3, 3))
  }
}
