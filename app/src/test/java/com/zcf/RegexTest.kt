package com.zcf

import org.junit.Assert
import org.junit.Test

class RegexTest {

  @Test
  fun testRegexMatches() {
    val placeholderRegex = Regex("%[123]\\$[sd]")
    val string = "abcd %1\$s efgh %2\$d ijkl %3\$s"
    var results = placeholderRegex.findAll(string)
    var placeholders = results.joinToString { it.value }
    Assert.assertEquals("%1\$s, %2\$d, %3\$s", placeholders)

    results = placeholderRegex.findAll("abcd efgh, ijkl, eref sdfdsf")
    placeholders = results.joinToString { it.value }
    Assert.assertEquals("", placeholders)
  }
}
