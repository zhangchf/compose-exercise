package com.zcf.composeexercise.pages

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zcf.composeexercise.R
import com.zcf.composeexercise.widget.BulletSpanText
import com.zcf.composeexercise.widget.setTextWithBulletSpan

@Composable
fun Texts() {
    Column {
        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    setTextWithBulletSpan(R.string.damagedBoxBody)
                }
            },
            modifier = Modifier.padding(20.dp)
        )

        BulletSpanText(
            textRes = R.string.damagedBoxBody1,
            modifier = Modifier.padding(20.dp)
        )
    }
}
