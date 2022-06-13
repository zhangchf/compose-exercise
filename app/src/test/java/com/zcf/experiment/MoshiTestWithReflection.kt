package com.zcf.experiment

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test

class MoshiTestWithReflection {

    data class RefModel1(
        val name: String,
        val age: Int
    )

    data class RefModel2(
        val name: String,
        val age: Int,
        val isValid: Boolean? = false
    )

    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Test
    fun `Deserialize on in-existed fields`() {
        val model1 = RefModel1("test", 12)

        val model1Adapter = moshi.adapter(RefModel1::class.java)
        val model1Json = model1Adapter.toJson(model1)
        println(model1Json)

        val model2Adapter = moshi.adapter(RefModel2::class.java)
        val model2 = model2Adapter.fromJson(model1Json)
        println(model2)
    }

}