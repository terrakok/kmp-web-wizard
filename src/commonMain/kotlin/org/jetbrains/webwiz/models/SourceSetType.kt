package org.jetbrains.webwiz.models

enum class SourceSetType(val sourceSetTypeName: String) {
    MAIN("Main"),
    TEST("Test")
}

enum class SourceSetDelegate(val delegate: String) {
    CREATING("creating"),
    GETTING("getting")
}