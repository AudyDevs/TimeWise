package com.example.timewise.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
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
    labelModel: LabelModel? = null,
    private var onClickButtonAdd: (LabelModel) -> Unit
) :
    Dialog(context) {

    companion object {
        const val INT_NULL = -1
    }

    private val binding = DialogLabelBinding.inflate(layoutInflater)
    private lateinit var labelColorAdapter: LabelColorAdapter
    private lateinit var labelImageAdapter: LabelImageAdapter
    private var textColor: Int = INT_NULL
    private var backcolor: Int = INT_NULL
    private var image: Int = INT_NULL
    private var idLabel: Int = 0

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(binding.root)
        initAdapters()
        initListeners()
        initLists()
        initDialogValues(labelModel)
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
            layoutManager = GridLayoutManager(context, 5)
            adapter = labelImageAdapter
        }
    }

    private fun initListeners() {
        binding.apply {
            imageIcon.setOnClickListener {
                etTitle.postDelayed({
                    etTitle.context.hideKeyboard(etTitle)
                }, 0)
                showLayoutIcons(true)
            }

            etTitle.addTextChangedListener { text ->
                enableButtonAdd(text.toString())
            }

            tvAdd.setOnClickListener {
                val labelModel = LabelModel(
                    id = idLabel,
                    image = image,
                    name = etTitle.text.toString(),
                    textColor = textColor,
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

            tvRemoveImage.setOnClickListener {
                image = INT_NULL
                imageIcon.setImageResource(R.drawable.ic_empty_image)
                if (backcolor != INT_NULL) {
                    imageIcon.imageTintList = ColorStateList.valueOf(backcolor)
                }
                showLayoutIcons(false)
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
                ImageModel.Anchor,
                ImageModel.Beach,
                ImageModel.Beer,
                ImageModel.Book,
                ImageModel.Cake,
                ImageModel.Camp,
                ImageModel.Card,
                ImageModel.Case,
                ImageModel.Castle,
                ImageModel.Cat,
                ImageModel.Cheese,
                ImageModel.Cinema,
                ImageModel.Clown,
                ImageModel.Coco,
                ImageModel.Coffee,
                ImageModel.Conch,
                ImageModel.Dessert,
                ImageModel.Dice,
                ImageModel.Dress,
                ImageModel.Earth,
                ImageModel.Flower,
                ImageModel.Fruit,
                ImageModel.Game,
                ImageModel.Gate,
                ImageModel.Girl,
                ImageModel.Graph,
                ImageModel.IceCream,
                ImageModel.Katana,
                ImageModel.Lemon,
                ImageModel.Letter,
                ImageModel.Meat,
                ImageModel.Monument,
                ImageModel.Moon,
                ImageModel.Museum,
                ImageModel.Mountain,
                ImageModel.Paint,
                ImageModel.Party,
                ImageModel.Pizza,
                ImageModel.Popcorn,
                ImageModel.Pumpkin,
                ImageModel.Radio,
                ImageModel.School,
                ImageModel.Shopping,
                ImageModel.Skull,
                ImageModel.Soap,
                ImageModel.Store,
                ImageModel.Sun,
                ImageModel.Tea,
                ImageModel.Ticket,
                ImageModel.Time,
                ImageModel.Van,
                ImageModel.Wine,
                ImageModel.Winner,
                ImageModel.Xmas,
                ImageModel.YinYang
            )
        )
    }

    private fun initDialogValues(labelModel: LabelModel?) {
        binding.apply {
            if (labelModel != null) {
                if (labelModel.id > 0) {
                    idLabel = labelModel.id
                    etTitle.setText(labelModel.name)

                    backcolor = labelModel.backcolor
                    textColor = labelModel.textColor
                    image = labelModel.image

                    if (image == INT_NULL) {
                        imageIcon.imageTintList = ColorStateList.valueOf(backcolor)
                    } else {
                        imageIcon.setImageResource(image)
                    }
                    labelTitle.setTextColor(textColor)
                    etTitle.backgroundTintList = ColorStateList.valueOf(backcolor)
                    enableButtonAdd(etTitle.text.toString())
                }
            }

            etTitle.requestFocus()
            etTitle.postDelayed({
                etTitle.context.showKeyboard(etTitle)
            }, 0)
        }
    }

    private fun changeUIColor(colorModel: ColorModel) {
        textColor = ContextCompat.getColor(context, colorModel.textColor)
        backcolor = ContextCompat.getColor(context, colorModel.backcolor)
        binding.apply {
            labelTitle.setTextColor(textColor)
            if (image == INT_NULL) {
                imageIcon.imageTintList = ColorStateList.valueOf(backcolor)
            }
            etTitle.backgroundTintList = ColorStateList.valueOf(backcolor)
            enableButtonAdd(etTitle.text.toString())
        }
    }

    private fun changeUIImage(imageModel: ImageModel) {
        image = imageModel.image
        binding.apply {
            showLayoutIcons(false)
            imageIcon.imageTintList = null
            imageIcon.setImageResource(image)
        }
    }

    private fun showLayoutIcons(show: Boolean) {
        binding.apply {
            rvLabelImages.isVisible = show
            rvLabelColors.isVisible = !show
            layoutRemoveImage.isVisible = show
            layoutAdd.isVisible = !show
        }
    }

    private fun enableButtonAdd(text: String) {
        binding.apply {
            tvAdd.isEnabled = (text.isNotEmpty()) and (textColor != INT_NULL)
            if (tvAdd.isEnabled) {
                tvAdd.setTextColor(textColor)
            } else {
                tvAdd.setTextColor(ContextCompat.getColor(context, R.color.disable))
            }
        }
    }
}