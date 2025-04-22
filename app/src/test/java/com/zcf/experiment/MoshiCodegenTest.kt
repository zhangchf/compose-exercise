package com.zcf.experiment

import com.squareup.moshi.Moshi
import org.junit.Test

class MoshiCodegenTest {

  val moshi = Moshi.Builder().build()

  @Test
  fun `Deserialize on in-existed fields`() {
    val model1 = Model1("test", 12)

    val model1Adapter = moshi.adapter(Model1::class.java)
    val model1Json = model1Adapter.toJson(model1)
    println(model1Json)

    val model2Adapter = moshi.adapter(Model2::class.java)
    val model2 = model2Adapter.fromJson(model1Json)
    println(model2)
  }
}
