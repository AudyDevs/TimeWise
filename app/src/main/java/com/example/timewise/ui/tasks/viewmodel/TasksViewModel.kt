package com.example.timewise.ui.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.usecase.DeleteLabelUseCase
import com.example.timewise.domain.usecase.GetLabelIdUseCase
import com.example.timewise.domain.usecase.UpdateLabelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getLabelIdUseCase: GetLabelIdUseCase,
    private val updateLabelUseCase: UpdateLabelUseCase,
    private val deleteLabelUseCase: DeleteLabelUseCase,
) : ViewModel() {

    private var _label = MutableStateFlow<LabelModel?>(null)
    var label: StateFlow<LabelModel?> = _label

    fun getLabelID(id: Int) {
        viewModelScope.launch {
            _label.value = withContext(Dispatchers.IO) { getLabelIdUseCase(id) }
        }
    }

    fun updateLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateLabelUseCase.invoke(label)
            }
        }
    }

    fun deleteLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteLabelUseCase.invoke(label)
            }
        }
    }
}