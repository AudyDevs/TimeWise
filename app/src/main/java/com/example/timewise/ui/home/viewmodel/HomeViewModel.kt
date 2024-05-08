package com.example.timewise.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.usecase.GetLabelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getLabelsUseCase: GetLabelsUseCase) :
    ViewModel() {

    private var _labels = MutableStateFlow<List<LabelModel>>(emptyList())
    var labels: StateFlow<List<LabelModel>> = _labels

    fun getLabels() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { _labels.value = getLabelsUseCase() }
        }
    }
}