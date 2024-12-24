package com.zcf.composeexercise

import androidx.core.text.parseAsHtml
import androidx.core.text.toHtml
import androidx.core.text.toSpanned
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test

class SpannableStringResourceTest {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun stringWithTagsAndArgs() {
        val string = appContext.getString(R.string.stringWithTagsAndArgs, "123.45")
        val text = appContext.getText(R.string.stringWithTagsAndArgs)
        val spanned = text.toSpanned()
        val spannedHtml = spanned.toHtml()
        val spannedHtmlStr = spannedHtml.format("123.45")
        val resultSpanned = spannedHtmlStr.parseAsHtml()
        resultSpanned
    }
}