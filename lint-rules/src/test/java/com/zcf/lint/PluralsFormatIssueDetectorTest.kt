package com.zcf.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

class PluralsFormatIssueDetectorTest : LintDetectorTest() {
  override fun getDetector() = PluralsFormatIssueDetector()

  override fun getIssues() = PluralsFormatIssueDetector.getIssues()

  fun testSuccessWhenPluralItemsMatchPlaceholders() {
    val stubFileEN = xml(
      "res/values/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order</item>
                    <item quantity="other">%1${dollar}d Orders</item>
                </plurals>
            </resources>
      """.trimIndent()
    )

    lint()
      .files(stubFileEN)
      .run()
      .expectClean()
  }

  fun testWarningWhenPluralItemIsEmpty() {
    val stubFileEN = xml(
      "res/values/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order</item>
                    <item quantity="many"/>
                    <item quantity="other">%1${dollar}d Orders</item>
                </plurals>
            </resources>
      """.trimIndent()
    )

    lint()
      .files(stubFileEN)
      .run()
      .expectWarningCount(1)
  }

  fun testErrorWhenPluralItemsNotMatchPlaceholders() {
    val stubFileEN = xml(
      "res/values/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order</item>
                    <item quantity="other">xxx Orders</item>
                </plurals>
            </resources>
      """.trimIndent()
    )

    lint()
      .files(stubFileEN)
      .run()
      .expectErrorCount(1)
  }

  fun testSuccessWhenPluralsTranslationsMatchPlaceholders() {
    val stubFileEN = xml(
      "res/values/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order</item>
                    <item quantity="other">%1${dollar}d Orders</item>
                </plurals>
            </resources>
      """.trimIndent()
    )
    val stubFileCN = xml(
      "res/values-zh/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order CN</item>
                    <item quantity="other">%1${dollar}d Orders CN</item>
                </plurals>
            </resources>
      """.trimIndent()
    )

    lint()
      .files(stubFileEN, stubFileCN)
      .run()
      .expectClean()
  }

  fun testErrorWhenPluralsTranslationsNotMatchPlaceholders() {
    val stubFileEN = xml(
      "res/values/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order</item>
                    <item quantity="other">%1${dollar}d Orders</item>
                </plurals>
            </resources>
      """.trimIndent()
    )
    val stubFileCN = xml(
      "res/values-zh/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">Order CN</item>
                    <item quantity="other">Orders CN</item>
                </plurals>
            </resources>
      """.trimIndent()
    )

    lint()
      .files(stubFileEN, stubFileCN)
      .run()
      .expectErrorCount(1)
  }

  fun testErrorWhenPluralsTranslationsNotMatchPlaceholdersEn() {
    val stubFileEN = xml(
      "res/values/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">Order</item>
                    <item quantity="other">Orders</item>
                </plurals>
            </resources>
      """.trimIndent()
    )
    val stubFileCN = xml(
      "res/values-zh/strings.xml",
      """
            <resources>
                <plurals name="ordersCounter">
                    <item quantity="one">%1${dollar}d Order CN</item>
                    <item quantity="other">%1${dollar}d Orders CN</item>
                </plurals>
            </resources>
      """.trimIndent()
    )

    lint()
      .files(stubFileEN, stubFileCN)
      .run()
      .expectErrorCount(1)
  }
}
