package org.jetbrains.webwiz

import org.w3c.files.Blob

@JsModule("file-saver")
@JsNonModule
external class FileSaverJs {
    companion object {
        fun saveAs(blob: Blob, fileName: String)
    }
}