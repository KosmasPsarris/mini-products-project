package com.kosmas.tecprin.utils

import android.widget.ProgressBar
import androidx.core.view.isVisible

fun ProgressBar.show() {
    isVisible = true
}

fun ProgressBar.hide() {
    isVisible = false
}
