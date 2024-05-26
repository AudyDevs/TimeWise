package com.example.timewise.ui.activities.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.core.DispatcherProvider
import com.example.timewise.core.FilterTypes
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.usecase.filteredtasks.GetNumberFilteredTasksUseCase
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.label.InsertLabelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getLabelsUseCase: GetLabelsUseCase,
    private val insertLabelUseCase: InsertLabelUseCase,
    private val getNumberFilteredTasksUseCase: GetNumberFilteredTasksUseCase
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
            _labels.value = withContext(dispatcherProvider.io) { getLabelsUseCase() }
        }
    }

    fun insertLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                insertLabelUseCase.invoke(label)
                getLabels()
            }
        }
    }

    fun getNumberTasks() {
        viewModelScope.launch {
            _tasksToday.value =
                withContext(dispatcherProvider.io) { getNumberFilteredTasksUseCase(FilterTypes.Today.type) }
            _tasksWeek.value =
                withContext(dispatcherProvider.io) { getNumberFilteredTasksUseCase(FilterTypes.Week.type) }
            _tasksLater.value =
                withContext(dispatcherProvider.io) { getNumberFilteredTasksUseCase(FilterTypes.Later.type) }
            _tasksExpired.value =
                withContext(dispatcherProvider.io) { getNumberFilteredTasksUseCase(FilterTypes.Expired.type) }
        }
    }
}