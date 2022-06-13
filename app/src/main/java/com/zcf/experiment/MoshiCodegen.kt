package com.zcf.experiment

import com.squareup.moshi.JsonClass

/**
 * To use Moshi codegen, must put class in src folder. not test folder
 */
@JsonClass(generateAdapter = true)
data class Model1(
    val name: String,
    val age: Int
)

@JsonClass(generateAdapter = true)
data class Model2(
    val name: String,
    val age: Int,
    val isValid: Boolean? = false
)