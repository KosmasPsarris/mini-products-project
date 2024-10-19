package com.kosmas.tecprin.utils

import android.app.Dialog
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.kosmas.tecprin.R

fun Dialog.addDim(amount: Float = 0.8f) {
    window?.apply {
        attributes.dimAmount = 0.8f
        addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}

fun Dialog.buildLayout() {
    window?.apply {
        setLayout(if (context.resources.configuration.smallestScreenWidthDp < 600)
            ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setGravity(Gravity.CENTER_VERTICAL)
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_dialog))
    }

}
