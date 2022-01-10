package org.jetbrains.webwiz

import org.w3c.dom.HTMLElement

@JsName("hljs")
@JsModule("highlight.js")
@JsNonModule
external class HighlightJs {
    companion object {
        fun highlight(code: String, lang: HighlightingLanguage): HighlightingResult
        fun highlightAuto(code: String): HighlightingResult
        fun highlightElement(block: HTMLElement)
    }
}

external interface HighlightingLanguage {
    var language: String
    var ignoreIllegals: Boolean?
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
private fun HighlightingLanguage(
    language: String,
    ignoreIllegals: Boolean = false
) = (js("{}") as HighlightingLanguage).apply {
    this.language = language
    this.ignoreIllegals = ignoreIllegals
}

external interface HighlightingResult {
    val value: String
}

//https://gist.github.com/hackjutsu/0a6338d66f4fd7d338fd0c04f3454394
private val commentPattern = Regex("""<span class="hljs-comment">(.|\n)*?</span>""")
private val mPattern = Regex("""\r?\n""")

fun HTMLElement.setHighlightedCode(code: String, language: String) {
    val highlightedContent = HighlightJs.highlight(code, HighlightingLanguage(language)).value
    val adaptedHighlightedContent = highlightedContent.replace(commentPattern) { match ->
        match.value.replace(mPattern, """\n<span class="hljs-comment">""")
    }
    val contentTable = adaptedHighlightedContent.lines().mapIndexed { i: Int, line: String ->
        """<tr><td data-pseudo-content='$i  '></td><td>$line</td></tr>"""
    }.joinToString("")
    innerHTML = """<table class='code-table'>$contentTable</table>"""
}