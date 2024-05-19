package com.example.timewise.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.label.InsertLabelUseCase
import com.example.timewise.domain.usecase.numbertasks.GetNumberTasksExpired
import com.example.timewise.domain.usecase.numbertasks.GetNumberTasksLater
import com.example.timewise.domain.usecase.numbertasks.GetNumberTasksToday
import com.example.timewise.domain.usecase.numbertasks.GetNumberTasksWeek
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
    private val insertLabelUseCase: InsertLabelUseCase,
    private val getNumberTasksToday: GetNumberTasksToday,
    private val getNumberTasksWeek: GetNumberTasksWeek,
    private val getNumberTasksLater: GetNumberTasksLater,
    private val getNumberTasksExpired: GetNumberTasksExpired
) :
    ViewModel() {

    private var _labels = MutableStateFlow<List<LabelModel>>(emptyList())
    var labels: StateFlow<List<LabelModel>> = _labels

    private var _tasksToday = MutableStateFlow(0)
    var tasksToday: StateFlow<Int> = _tasksToday

    private var _tasksWeek = MutableStateFlow(0)
    var tasksWeek: StateFlow<Int> = _tasksWeek

    private var _tasksLater = MutableStateFlow(0)
    var tasksLater: StateFlow<Int> = _tasksLater

    private var _tasksExpired = MutableStateFlow(0)
    var tasksExpired: StateFlow<Int> = _tasksExpired

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

    fun getNumberTasks() {
        viewModelScope.launch {
            _tasksToday.value = withContext(Dispatchers.IO) { getNumberTasksToday() }
            _tasksWeek.value = withContext(Dispatchers.IO) { getNumberTasksWeek() }
            _tasksLater.value = withContext(Dispatchers.IO) { getNumberTasksLater() }
            _tasksExpired.value = withContext(Dispatchers.IO) { getNumberTasksExpired() }
        }
    }
}