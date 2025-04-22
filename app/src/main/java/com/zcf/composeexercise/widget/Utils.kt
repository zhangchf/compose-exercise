package com.zcf.composeexercise.widget

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.view.View

fun Resources.dp2px(size: Int): Float = size * displayMetrics.density
fun View.dp2px(size: Int): Int = resources.dp2px(size).toInt()

fun Context.openAppSettings() {
  val intent = Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", this.packageName, null)
  )
  startActivity(intent)
}
