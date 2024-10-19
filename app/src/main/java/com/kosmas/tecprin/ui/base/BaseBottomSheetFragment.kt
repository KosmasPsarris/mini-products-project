package com.kosmas.tecprin.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kosmas.tecprin.R
import com.kosmas.tecprin.utils.addDim
import com.kosmas.tecprin.utils.buildLayout

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    fun showDialog(
        title: String? = null,
        titleColor: String? = null,
        message: String? = null,
        optionalButton: String? = null,
        mandatoryButton: String?,
        optionalAction: () -> Unit = {},
        mandatoryAction: () -> Unit = {},
        isCancelable: Boolean = false
    ) {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.apply {
            setContentView(R.layout.base_dialog)
            title?.let {
                findViewById<TextView>(R.id.dialogTitle).apply {
                    text = it
                    titleColor?.let {
                        setTextColor(Color.parseColor(it))
                    }
                }
            }
            message?.let {
                findViewById<TextView>(R.id.dialogMessage).apply {
                    text = it
                }
            }
            optionalButton?.let {
                findViewById<Button>(R.id.optionalButton).apply {
                    isVisible = true
                    text = it
                    setOnClickListener {
                        dismiss()
                        optionalAction()
                    }
                }
            }
            findViewById<Button>(R.id.mandatoryButton).apply {
                text = mandatoryButton
                setOnClickListener {
                    dismiss()
                    mandatoryAction()
                }
            }
            setCancelable(isCancelable)
            show()
            addDim()
            buildLayout()
        }
    }

}
