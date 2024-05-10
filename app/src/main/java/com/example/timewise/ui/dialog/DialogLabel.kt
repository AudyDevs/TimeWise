package com.example.timewise.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.timewise.R
import com.example.timewise.core.extensions.hideKeyboard
import com.example.timewise.core.extensions.showKeyboard
import com.example.timewise.databinding.DialogLabelBinding
import com.example.timewise.domain.model.LabelModel

class DialogLabel(
    context: Context,
    private var onClickButtonAdd: (LabelModel) -> Unit
) :
    Dialog(context) {

    private val binding = DialogLabelBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)

        binding.apply {
            etTitle.requestFocus()
            etTitle.postDelayed({
                etTitle.context.showKeyboard(etTitle)
            }, 0)

        }
        initListeners()
        show()
    }

    private fun initListeners() {
        binding.apply {
            tvAdd.setOnClickListener {
                val labelModel = LabelModel(
                    id = 0,
                    image = R.drawable.ic_empty_image,
                    name = etTitle.text.toString(),
                    numberIncomplete = 0,
                    color = R.color.textColorPurple,
                    backcolor = R.color.backColorPurple
                )
                onClickButtonAdd(labelModel)
            }
            tvCancel.setOnClickListener {
                etTitle.postDelayed({
                    etTitle.context.hideKeyboard(etTitle)
                }, 0)
                dismiss()
            }
        }
    }
}