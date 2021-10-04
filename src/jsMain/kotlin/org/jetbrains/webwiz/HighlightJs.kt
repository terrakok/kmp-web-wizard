package org.jetbrains.webwiz

import org.w3c.dom.HTMLElement

@JsName("hljs")
@JsModule("highlight.js")
@JsNonModule
external class HighlightJs {
    companion object {
        fun highlightElement(block: HTMLElement)
    }
}

fun HTMLElement.setHighlightedCode(code: String) {
    innerText = code
    HighlightJs.highlightElement(this)
}