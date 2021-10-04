package org.jetbrains.webwiz.content

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.selectors.CSSSelector
import org.jetbrains.compose.web.css.selectors.descendant
import org.jetbrains.compose.web.css.selectors.selector
import org.jetbrains.webwiz.style.AppStylesheet
import org.jetbrains.webwiz.models.KotlinVersion
import org.jetbrains.compose.web.dom.*

private object SwitcherVariables {
    val labelWidth by variable<CSSpxValue>()
    val labelPadding by variable<CSSpxValue>()
}

@Composable
fun KotlinVersionSwitcher(versions: Array<KotlinVersion>, state: Map<KotlinVersion, Boolean>, onSelect: (KotlinVersion) -> Unit) {
    Form(attrs = {
        classes(KotlinSwitcherStylesheet.boxed)
    }) {
        versions.forEach { version ->
            Input(type = InputType.Radio, attrs = {
                name("code-snippet")
                value("snippet$version")
                id("snippet$version")
                checked(state[version]!!)
                onClick { onSelect(version) }
            })
            Label(forId = "snippet$version") { Text("${version.name}") }
        }
    }
}


object KotlinSwitcherStylesheet : StyleSheet(AppStylesheet) {
    val boxed by style {

        media(mediaMaxWidth(202.px)) {
            self style {
                SwitcherVariables.labelWidth(36.px)
                SwitcherVariables.labelPadding(5.px)
            }
        }

        backgroundColor(Color("rgb(244,244,244)"))

        descendant(self, CSSSelector.Type("label")) style {
            display(DisplayStyle.InlineBlock)
            width(SwitcherVariables.labelWidth.value(100.px))
            padding(SwitcherVariables.labelPadding.value(5.px))
            property("transition", "all 0.3s")
            textAlign("center")
            boxSizing("border-box")

            border {
                style(LineStyle.Solid)
                width(3.px)
                color(Color("transparent"))
                borderRadius(20.px, 20.px, 20.px)
            }

        }

        border {
            style(LineStyle.Solid)
            width(0.px)
            color(Color("#aaa"))
            padding(0.px)
            borderRadius(22.px, 22.px, 22.px)
        }

        descendant(self, selector("input[type=\"radio\"]")) style {
            display(DisplayStyle.None)

        }

        descendant(self, selector("input[type=\"radio\"]:checked + label")) style {
            border {
                style(LineStyle.Solid)
                width(3.px)
                color(Color("#167dff"))
                property("background-color", "#167dff")
                borderRadius(20.px, 20.px, 20.px)
            }
            color(Color("white"))
        }
    }
}