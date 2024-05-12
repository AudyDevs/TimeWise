package com.example.timewise.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.usecase.GetLabelsUseCase
import com.example.timewise.domain.usecase.InsertLabelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLabelsUseCase: GetLabelsUseCase,
    private val insertLabelUseCase: InsertLabelUseCase
) :
    ViewModel() {

    private var _labels = MutableStateFlow<List<LabelModel>>(emptyList())
    var labels: StateFlow<List<LabelModel>> = _labels

    fun getLabels() {
        viewModelScope.launch {
            _labels.value = withContext(Dispatchers.IO) { getLabelsUseCase() }
        }
    }

    fun insertLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertLabelUseCase.invoke(label)
                getLabels()
            }
        }
    }
}