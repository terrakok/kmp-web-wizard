package org.jetbrains.webwiz.style

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*

object WtOffsets : StyleSheet(AppStylesheet) {
    val wtTopOffset96 by style {
        marginTop(96.px)
        property(
            "margin-top",
            "calc(4*${AppCSSVariables.wtOffsetTopUnit.value(24.px)})"
        )
    }

    val wtTopOffset24 by style {
        marginTop(24.px)
        property(
            "margin-top",
            "calc(1*${AppCSSVariables.wtOffsetTopUnit.value(24.px)})"
        )
    }

    val wtTopOffset48 by style {
        marginTop(48.px)
    }

    val wtTopOffsetSm12 by style {
        media(mediaMaxWidth(640.px)) {
            self style {
                marginTop(12.px)
            }
        }
    }

    val wtTopOffsetSm24 by style {
        media(mediaMaxWidth(640.px)) {
            self style {
                marginTop(24.px)
            }
        }
    }

    val textInputLabelsStyle by style {
        width(20.percent)
        display(DisplayStyle.InlineBlock)
    }


    val textInputStyle by style {
        property("border", "1px solid rgb(170, 170, 170, 42%)")
        property("color", "rgb(39 40 44 / 79%)")
        property(
            "font-famyly",
            "system-ui,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Oxygen,Ubuntu,Cantarell,Droid Sans,Helvetica Neue,Arial,sans-serif"
        )
        fontSize(15.px)
        width(70.percent)
        padding(10.px)
    }


    val rowItems by style {
        display(DisplayStyle.LegacyInlineFlex)
        alignItems(AlignItems.Center)
        marginBottom(16.px)
        width(100.percent)
    }

    val rowTargetsItems by style {
        display(DisplayStyle.LegacyInlineFlex)
        alignItems("top")
        marginBottom(16.px)
        width(100.percent)
    }

    val targetsCheckboxesListStyle by style {
        display(DisplayStyle.Flex)
        property("flex-wrap", "wrap")
        width(77.percent)
    }
    val targetsCheckboxesStyle by style {
        marginBottom(10.px)
        marginRight(10.px)

        desc(self, selector("input[type=\"checkbox\"]")) style {
            display(DisplayStyle.None)
        }

        desc(self, selector("input[type=\"checkbox\"] + label")) style {
            property("-webkit-transition", "all 500ms ease")
            property("transition", "all 500ms ease")
            property("font-size", "18px")
            property("background-color", "rgb(244,244,244)")
            property("padding", "0.5rem 2rem")
            property("-moz-user-select", "-moz-none")
            property("-ms-user-select", "none")
            property("-webkit-user-select", "none")
            property("user-select", "none")
            cursor("pointer")
            borderRadius(50.px)
            display(DisplayStyle.InlineBlock);
        }

        desc(self, selector("input[type=\"checkbox\"]:checked + label")) style {
            property("-webkit-transition: all", "500ms ease")
            property("transition", "all 300ms ease")
            property("background-color", "#167dff")
            property("color", "white")
            property("border-color", "#167dff")
        }

        desc(self, selector("input[type=\"checkbox\"]:disabled + label")) style {
            property("-webkit-transition: all", "500ms ease")
            property("transition", "all 300ms ease")
            property("background-color", "rgb(244,244,244)")
            property("color", "#bfbfbf")
            property("cursor", "not-allowed")
        }
    }

    @OptIn(ExperimentalComposeWebApi::class)
    val testsCheckboxStyle by style {

        desc(self, selector("input[type=\"checkbox\"]")) style {
            property("box-sizing", "border-box")
            padding(0.px)
            transform { scale(1.5) }
        }

        desc(self, selector("input[type=\"checkbox\"]:checked")) style {
            property("box-sizing", "border-box")
            padding(0.px)
            property("border-color", "#aaa")
            property("background-color", "#aaa")
        }
    }

    val generateButtonStyle by style {
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
    }
}
