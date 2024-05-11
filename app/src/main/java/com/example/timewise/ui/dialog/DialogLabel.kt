package com.example.timewise.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.core.extensions.hideKeyboard
import com.example.timewise.core.extensions.showKeyboard
import com.example.timewise.databinding.DialogLabelBinding
import com.example.timewise.domain.model.ColorModel
import com.example.timewise.domain.model.ImageModel
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.ui.dialog.adapter.LabelColorAdapter
import com.example.timewise.ui.dialog.adapter.LabelImageAdapter

class DialogLabel(
    context: Context,
    private var onClickButtonAdd: (LabelModel) -> Unit
) :
    Dialog(context) {

    private val binding = DialogLabelBinding.inflate(layoutInflater)
    private lateinit var labelColorAdapter: LabelColorAdapter
    private lateinit var labelImageAdapter: LabelImageAdapter
    private var textColor: Int = -1
    private var backcolor: Int = -1
    private var image: Int = -1

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
        initAdapters()
        initListeners()
        initLists()
        show()
    }

    private fun initAdapters() {
        labelColorAdapter = LabelColorAdapter(onItemSelected = { colorModel ->
            changeUIColor(colorModel)
        })
        binding.rvLabelColors.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = labelColorAdapter
        }

        labelImageAdapter = LabelImageAdapter(onItemSelected = { imageModel ->
            changeUIImage(imageModel)
        })

        binding.rvLabelImages.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = labelImageAdapter
        }
    }

    private fun initListeners() {
        binding.apply {
            imageIcon.setOnClickListener {
                etTitle.postDelayed({
                    etTitle.context.hideKeyboard(etTitle)
                }, 0)
                rvLabelImages.isVisible = true
                rvLabelColors.isVisible = false
            }

            tvAdd.setOnClickListener {
                val labelModel = LabelModel(
                    id = 0,
                    image = image,
                    name = etTitle.text.toString(),
                    numberIncomplete = 0,
                    color = textColor,
                    backcolor = backcolor
                )
                onClickButtonAdd(labelModel)
                dismiss()
            }

            tvCancel.setOnClickListener {
                etTitle.postDelayed({
                    etTitle.context.hideKeyboard(etTitle)
                }, 0)
                dismiss()
            }
        }
    }

    private fun initLists() {
        initColors()
        initImages()
    }

    private fun initColors() {
        labelColorAdapter.updateList(
            listOf(
                ColorModel.Yellow,
                ColorModel.Orange,
                ColorModel.Red,
                ColorModel.Purple,
                ColorModel.Blue,
                ColorModel.LightBlue,
                ColorModel.Cyan,
                ColorModel.Green
            )
        )
    }

    private fun initImages() {
        labelImageAdapter.updateList(
            listOf(
                ImageModel.Add, ImageModel.Calendar, ImageModel.EmptyFace,
                ImageModel.Add, ImageModel.Calendar, ImageModel.EmptyFace,
                ImageModel.Add, ImageModel.Calendar, ImageModel.EmptyFace,
                ImageModel.Add, ImageModel.Calendar, ImageModel.EmptyFace,
                ImageModel.Add, ImageModel.Calendar, ImageModel.EmptyFace,
                ImageModel.Add, ImageModel.Calendar, ImageModel.EmptyFace
            )
        )
    }

    private fun changeUIColor(colorModel: ColorModel) {
        textColor = ContextCompat.getColor(context, colorModel.textColor)
        backcolor = ContextCompat.getColor(context, colorModel.backcolor)
        binding.apply {
            labelTitle.setTextColor(textColor)
            if(image == -1){
                imageIcon.imageTintList = ColorStateList.valueOf(backcolor)
            }
            etTitle.backgroundTintList = ColorStateList.valueOf(backcolor)
            tvAdd.setTextColor(textColor)
        }
    }

    private fun changeUIImage(imageModel: ImageModel) {
        image = imageModel.image
        binding.apply {
            rvLabelImages.isVisible = false
            rvLabelColors.isVisible = true
            imageIcon.imageTintList = null
            imageIcon.setImageResource(image)
        }
    }
}