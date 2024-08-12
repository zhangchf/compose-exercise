package com.zcf.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

class StringFormatIssueDetectorTest : LintDetectorTest() {
    override fun getDetector() = StringFormatIssueDetector()

    override fun getIssues() = StringFormatIssueDetector.getIssues()

    private val dollar = "$"

    fun testNoErrorWhenCheckStringsWithNoArguments() {
        val stubFileEN = xml(
            "res/values/strings.xml",
            """
            <resources>
              <string name="key1">EN string 1</string>
              <string name="key2">EN string 2</string>
            </resources>
            """.trimIndent()
        )
        val stubFileCN = xml(
            "res/values-zh/strings.xml",
            """
            <resources>
              <string name="key1">CN string 1</string>
              <string name="key2">CN string 2</string>
            </resources>
            """.trimIndent()
        )

        lint()
            .files(stubFileEN, stubFileCN)
            .run()
            .expectClean()
    }

    fun testNoErrorWhenStringsHaveCorrectPlaceholders() {
        val stubFileEN = xml(
            "res/values/strings.xml",
            """
            <resources>
              <string name="key1">EN string %1${dollar}s xxx %1${dollar}d xxx</string>
              <string name="key2">EN string %1${dollar}d yyy %1${dollar}s yyy</string>
            </resources>
            """.trimIndent()
        )
        val stubFileCN = xml(
            "res/values-zh/strings.xml",
            """
            <resources>
              <string name="key1">CN string %1${dollar}s xxx %1${dollar}d xxx</string>
              <string name="key2">CN string %1${dollar}d yyy %1${dollar}s yyy</string>
            </resources>
            """.trimIndent()
        )

        lint()
            .files(stubFileEN, stubFileCN)
            .run()
            .expectClean()
    }

    fun testOrdersOfPlaceholderDoNotMatter() {
        val stubFileEN = xml(
            "res/values/strings.xml",
            """
            <resources>
              <string name="key1">EN string %1${dollar}s xxx %1${dollar}d xxx</string>
            </resources>
            """.trimIndent()
        )
        val stubFileCN = xml(
            "res/values-zh/strings.xml",
            """
            <resources>
              <string name="key1">CN string %1${dollar}d xxx %1${dollar}s xxx</string>
            </resources>
            """.trimIndent()
        )

        lint()
            .files(stubFileEN, stubFileCN)
            .run()
            .expectClean()
    }

    fun testErrorWhenPlaceholdersDoNotMatch() {
        val stubFileEN = xml(
            "res/values/strings.xml",
            """
            <resources>
              <string name="key1">EN string %1${dollar}s xxx %1${dollar}d xxx</string>
            </resources>
            """.trimIndent()
        )
        val stubFileCN = xml(
            "res/values-zh/strings.xml",
            """
            <resources>
              <string name="key1">CN string %1${dollar}d xxx %1${dollar}d xxx</string>
            </resources>
            """.trimIndent()
        )

        lint()
            .files(stubFileEN, stubFileCN)
            .run()
            .expectErrorCount(1)
    }

    fun testErrorWhenPlaceholdersCountDifferent() {
        val stubFileEN = xml(
            "res/values/strings.xml",
            """
            <resources>
              <string name="key1">EN string %1${dollar}s xxx %1${dollar}d xxx</string>
            </resources>
            """.trimIndent()
        )
        val stubFileCN = xml(
            "res/values-zh/strings.xml",
            """
            <resources>
              <string name="key1">CN string %1${dollar}d xxx xxx</string>
            </resources>
            """.trimIndent()
        )

        lint()
            .files(stubFileEN, stubFileCN)
            .run()
            .expectErrorCount(1)
    }

    fun testErrorWhenEnHaveNoPlaceholderButOthersHave() {
        val stubFileEN = xml(
            "res/values/strings.xml",
            """
            <resources>
              <string name="key1">EN string xxx xxx</string>
            </resources>
            """.trimIndent()
        )
        val stubFileCN = xml(
            "res/values-zh/strings.xml",
            """
            <resources>
              <string name="key1">CN string %1${dollar}d xxx xxx</string>
            </resources>
            """.trimIndent()
        )

        lint()
            .files(stubFileEN, stubFileCN)
            .run()
            .expectErrorCount(1)
    }
}