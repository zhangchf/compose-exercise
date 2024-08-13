package com.zcf.lint

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.VALUES
import com.android.tools.lint.detector.api.Category.Companion.MESSAGES
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Location.Handle
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope.Companion.ALL_RESOURCES_SCOPE
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.Severity.WARNING
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.forEach
import com.android.utils.text
import org.w3c.dom.Element

class PluralsFormatIssueDetector : ResourceXmlDetector(), SourceCodeScanner {

  private data class ElementInfo(val text: String, val placeholders: String, val folder: String, val handle: Handle)

  private var elementInfoMap: MutableMap<String, MutableList<ElementInfo>> = mutableMapOf()

  private fun List<ElementInfo>.baseElement() = firstOrNull { it.folder == "values" }

  override fun appliesTo(folderType: ResourceFolderType): Boolean {
    return folderType == VALUES
  }

  override fun getApplicableElements(): Collection<String> {
    return listOf(SdkConstants.TAG_PLURALS)
  }

  override fun visitElement(context: XmlContext, element: Element) {
    val name = element.getAttribute(SdkConstants.ATTR_NAME)
    val handle = context.createLocationHandle(element)

    val itemMap = mutableMapOf<String, String>()
    element.getElementsByTagName(SdkConstants.TAG_ITEM).forEach { node ->
      (node as? Element)?.let { item ->
        val quantity = item.getAttribute(SdkConstants.ATTR_QUANTITY)
        if (item.text().isBlank()) {
          context.report(
            issue = EMPTY_PLURALS_ITEM,
            location = context.createLocationHandle(item).resolve(),
            message = "Empty plurals item for ($name:$quantity): ${item.text()}"
          )
        } else {
          val placeholders =
            placeholderRegex.findAll(item.text()).map { it.value }.toList().sorted().joinToString()
          itemMap[quantity] = placeholders
        }
      }
    }

    if (itemMap.isNotEmpty()) {
      val basePlaceholders = itemMap.entries.first().value
      val incorrectIndex = itemMap.entries.indexOfFirst { it.value != basePlaceholders }
      if (incorrectIndex != -1) {
        context.report(
          issue = UNMATCHED_PLURALS_FORMAT_PLACEHOLDERS,
          location = handle.resolve(),
          message = "Incorrect placeholders for plurals ($name): ${element.text()}"
        )
      } else {
        if (basePlaceholders.isNotEmpty() || elementInfoMap[name] != null) {
          val folder = context.file.parentFile.name
          elementInfoMap[name] = (elementInfoMap[name] ?: mutableListOf()).also {
            it.add(ElementInfo(element.text(), basePlaceholders, folder, handle))
          }
        }
      }
    }
  }

  override fun afterCheckRootProject(context: Context) {
    println("PluralsFormatIssueDetector: check plurals formats")
    for (entry in elementInfoMap.entries) {
      val name = entry.key
      val list = entry.value

      val baseElement = list.baseElement()
      val basePlaceholders = baseElement?.placeholders
      val incorrectIndex = list.indexOfFirst { it.placeholders != basePlaceholders }
      if (incorrectIndex != -1) {
        val elementInfo = list[incorrectIndex]
        context.report(
          issue = UNMATCHED_PLURALS_FORMAT_PLACEHOLDERS,
          location = elementInfo.handle.resolve(),
          message = "Incorrect placeholders for plurals($name): ${elementInfo.text} <---> ${baseElement?.text ?: ""}"
        )
      }
    }
  }

  companion object {
    private val IMPLEMENTATION_XML = Implementation(
      PluralsFormatIssueDetector::class.java, ALL_RESOURCES_SCOPE
    )
    val UNMATCHED_PLURALS_FORMAT_PLACEHOLDERS: Issue = Issue.create(
      "UnmatchedPluralsPlaceholders",
      "Formatting placeholders are inconsistent among plurals items or translations",
      "Plurals format placeholders in plural items or translations doesn't match",
      MESSAGES,
      5,
      ERROR,
      IMPLEMENTATION_XML
    )

    val EMPTY_PLURALS_ITEM: Issue = Issue.create(
      "EmptyPluralsItem",
      "Plurals item is empty",
      "Plurals item is empty, which might cause crash",
      MESSAGES,
      5,
      WARNING,
      IMPLEMENTATION_XML
    )

    fun getIssues(): List<Issue> = listOf(UNMATCHED_PLURALS_FORMAT_PLACEHOLDERS, EMPTY_PLURALS_ITEM)
  }
}
