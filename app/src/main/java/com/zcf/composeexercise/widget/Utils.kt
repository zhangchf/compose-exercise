package com.zcf.composeexercise.widget

import android.content.res.Resources
import android.view.View

fun Resources.dp2px(size: Int): Float = size * displayMetrics.density
fun View.dp2px(size: Int): Int = resources.dp2px(size).toInt()