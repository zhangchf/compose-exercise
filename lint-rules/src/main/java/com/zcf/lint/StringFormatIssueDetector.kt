package org.alias.lint

import com.android.AndroidXConstants.CLASS_V4_FRAGMENT
import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.resources.ResourceFolderType.VALUES
import com.android.resources.ResourceType.STRING
import com.android.tools.lint.client.api.TYPE_STRING
import com.android.tools.lint.detector.api.Category.Companion.MESSAGES
import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Location.Handle
import com.android.tools.lint.detector.api.ResourceEvaluator
import com.android.tools.lint.detector.api.ResourceXmlDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Scope.ALL_RESOURCE_FILES
import com.android.tools.lint.detector.api.Scope.Companion.ALL_RESOURCES_SCOPE
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.XmlContext
import com.android.utils.text
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.w3c.dom.Element
import java.util.Arrays
import java.util.EnumSet

/**
 * Reference:
 * https://proandroiddev.com/implementing-your-first-android-lint-rule-6e572383b292
 * https://proandroiddev.com/testing-your-first-android-lint-rule-bb78c1f2d2bd
 * Reference:
 * https://android.googlesource.com/platform/tools/base/+/studio-master-dev/lint/libs/lint-checks/src/main/java/com/android/tools/lint/checks/StringFormatDetector.java?autodive=0%2F
 */
class StringFormatIssueDetector : ResourceXmlDetector(), SourceCodeScanner {

  private data class ElementInfo(val value: String, val placeholders: List<String>, val handle: Handle)

  private val placeholderRegex = Regex("%[123]\\$[sd]")

  private var elementInfoMap: MutableMap<String, MutableList<ElementInfo>> = mutableMapOf()

  override fun appliesTo(folderType: ResourceFolderType): Boolean {
    return folderType == VALUES
  }

  override fun getApplicableElements(): Collection<String> {
    return listOf(SdkConstants.TAG_STRING)
  }

  override fun getApplicableMethodNames(): List<String>? {
    return Arrays.asList(SdkConstants.FORMAT_METHOD, SdkConstants.GET_STRING_METHOD)
  }

  override fun visitElement(context: XmlContext, element: Element) {
    val name = element.getAttribute(SdkConstants.ATTR_NAME)
    val value = element.text()
    val placeholders = placeholderRegex.findAll(value).map { it.value }.toList().sorted()

    if (placeholders.isNotEmpty()) {
      val handle = context.createLocationHandle(element)
      elementInfoMap[name] = (elementInfoMap[name] ?: mutableListOf()).also {
        it.add(ElementInfo(value, placeholders, handle))
      }
    }
  }

  override fun afterCheckRootProject(context: Context) {
    println("StringFormatIssueDetector: check string formats")
    for (entry in elementInfoMap.entries) {
      val name = entry.key
      val list = entry.value

      val basePlaceholders = list[0].placeholders.joinToString()
      val incorrectIndex = list.indexOfFirst { it.placeholders.joinToString() != basePlaceholders }
      if (incorrectIndex != -1) {
        val elementInfo = list[incorrectIndex]
        context.report(
          issue = UNMATCHED_FORMAT_PLACEHOLDERS,
          location = elementInfo.handle.resolve(),
          message = "Incorrect placeholders for $name: ${elementInfo.value} <---> ${list[0].value}"
        )
      }
    }
  }

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val evaluator = context.evaluator
    val methodName = method.name
    if (methodName == SdkConstants.FORMAT_METHOD) {
      if (evaluator.isMemberInClass(method, TYPE_STRING)) {
        // Check formatting parameters for
        //   java.lang.String#format(String format, Object... formatArgs)
        //   java.lang.String#format(Locale locale, String format, Object... formatArgs)
        checkStringFormatCall(
          context, method, node, method.parameterList.parametersCount == 3
        )
      }
    } else {
      // Look up any of these string formatting methods:
      // android.content.res.Resources#getString(@StringRes int resId, Object... formatArgs)
      // android.content.Context#getString(@StringRes int resId, Object... formatArgs)
      // android.app.Fragment#getString(@StringRes int resId, Object... formatArgs)
      // android.support.v4.app.Fragment#getString(@StringRes int resId, Object... formatArgs)
      // Many of these also define a plain getString method:
      // android.content.res.Resources#getString(@StringRes int resId)
      // However, while it's possible that these contain formatting strings) it's
      // also possible that they're looking up strings that are not intended to be used
      // for formatting so while we may want to warn about this it's not necessarily
      // an error.
      if (method.parameterList.parametersCount < 2) {
        return
      }
      if (evaluator.isMemberInSubClassOf(method, SdkConstants.CLASS_RESOURCES, false)
        || evaluator.isMemberInSubClassOf(method, SdkConstants.CLASS_CONTEXT, false)
        || evaluator.isMemberInSubClassOf(method, SdkConstants.CLASS_FRAGMENT, false)
        || evaluator.isMemberInSubClassOf(method, CLASS_V4_FRAGMENT.oldName(), false)
        || evaluator.isMemberInSubClassOf(method, CLASS_V4_FRAGMENT.newName(), false)
      ) {
        checkStringFormatCall(context, method, node, false)
      }
    }
  }

  /**
   * Check the given String.format call (with the given arguments) to see if the string format is
   * being used correctly
   *
   * @param context the context to report errors to
   * @param calledMethod the method being called
   * @param call the AST node for the [String.format]
   * @param specifiesLocale whether the first parameter is a locale string, shifting the
   */
  private fun checkStringFormatCall(
    context: JavaContext,
    calledMethod: PsiMethod,
    call: UCallExpression,
    specifiesLocale: Boolean
  ) {
    val argIndex = if (specifiesLocale) 1 else 0
    val args = call.valueArguments
    if (args.size <= argIndex) {
      return
    }
    val callCount = args.size - 1 - argIndex

    val argument = args[argIndex]
    val resource = ResourceEvaluator.getResource(context.evaluator, argument)
    if (resource == null || resource.isFramework || resource.type != STRING) {
      return
    }
    val name = resource.name
    val argCount = elementInfoMap[name]?.firstOrNull()?.placeholders?.size ?: 0

    if (callCount != argCount) {
      context.report(
        issue = UNMATCHED_FORMAT_PARAMTERS,
        scope = call,
        location = context.getLocation(call),
        message = "Wrong argument count, $name requires $argCount but supplied $callCount."
      )
    }
  }

  companion object {
    private val IMPLEMENTATION_XML = Implementation(
      StringFormatIssueDetector::class.java, ALL_RESOURCES_SCOPE
    )
    private val IMPLEMENTATION_XML_AND_JAVA = Implementation(
      StringFormatIssueDetector::class.java,
      EnumSet.of<Scope>(ALL_RESOURCE_FILES, JAVA_FILE),
      JAVA_FILE_SCOPE
    )

    val UNMATCHED_FORMAT_PLACEHOLDERS: Issue = Issue.create(
      "UnmatchedStringPlaceholders",
      "Formatting placeholders are inconsistent among translations",
      "String format placeholders in translations doesn't match",
      MESSAGES,
      5,
      ERROR,
      IMPLEMENTATION_XML
    )

    val UNMATCHED_FORMAT_PARAMTERS: Issue = Issue.create(
      "InvalidStringFormatParameterCount",
      "String format parameters doesn't match placeholders",
      "String format parameters and placeholders doesn't match",
      MESSAGES,
      9,
      ERROR,
      IMPLEMENTATION_XML_AND_JAVA
    )

    fun getIssues(): List<Issue> = listOf(UNMATCHED_FORMAT_PLACEHOLDERS, UNMATCHED_FORMAT_PARAMTERS)
  }
}
