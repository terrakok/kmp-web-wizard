package org.jetbrains.webwiz.content

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.webwiz.components.ContainerInSection
import org.jetbrains.webwiz.style.AppStylesheet
import org.jetbrains.webwiz.style.WtCols
import org.jetbrains.webwiz.style.WtOffsets
import org.jetbrains.webwiz.style.WtRows
import org.jetbrains.webwiz.style.WtTexts

@Composable
fun Intro() {
    ContainerInSection {
        Div({
            classes(WtRows.wtRow, WtRows.wtRowSizeM, WtRows.wtRowSmAlignItemsCenter)
        }) {

            Div({
                classes(WtCols.wtCol2, WtCols.wtColMd3)
                style {
                    alignSelf(AlignSelf.Start)
                }
            }) {
                Img(src = "ic_kotlin_logo.svg", attrs = { classes(AppStylesheet.composeLogo) })
            }

            Div({
                classes(
                    WtCols.wtCol10,
                    WtCols.wtColMd8,
                    WtCols.wtColSm12,
                    WtOffsets.wtTopOffsetSm12
                )
            }) {
                H1(attrs = { classes(WtTexts.wtHero) }) {
                    Text("Kotlin Multiplatform Wizard")
                }
                Div {
                    IntroAboutKMP()
                }
            }
        }

    }
}

@Composable
private fun IntroAboutKMP() {
    Div({
        classes(WtRows.wtRow, WtRows.wtRowSizeM)
    }) {
        Div({
            classes(WtCols.wtCol9, WtCols.wtColMd9, WtCols.wtColSm12)
        }) {
            P({ classes(WtTexts.wtSubtitle2, WtOffsets.wtTopOffset24) }) {
                Text("Support for multiplatform programming is one of Kotlin’s key benefits. ")

                Text("Learn more about ")

                A(href = "https://kotlinlang.org/docs/multiplatform.html", attrs = {
                    classes(WtTexts.wtLink)
                    target(ATarget.Blank)
                }) {
                    Text("Kotlin Multiplatform benefits")
                }
            }
        }
    }
}