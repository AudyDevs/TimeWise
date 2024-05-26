package com.example.timewise.ui.activities.filtered.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.core.DispatcherProvider
import com.example.timewise.core.FilterTypes
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.domain.usecase.filteredtasks.GetFilteredTasksUseCase
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FilteredTasksViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getFilteredTasksUseCase: GetFilteredTasksUseCase,
    private val getLabelsUseCase: GetLabelsUseCase,
    private val updateTaskFinishedUseCase: UpdateTaskFinishedUseCase,
    private val updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase
) : ViewModel() {

    private var _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    var tasks: StateFlow<List<TaskModel>> = _tasks

    private var _labels = MutableStateFlow<List<LabelModel>>(emptyList())
    var labels: StateFlow<List<LabelModel>> = _labels

    var filterTypes: String = FilterTypes.Today.type

    fun getFilteredTasks() {
        viewModelScope.launch {
            _tasks.value =
                withContext(dispatcherProvider.io) { getFilteredTasksUseCase(filterTypes) }
        }
    }

    fun getLabels() {
        viewModelScope.launch {
            _labels.value = withContext(dispatcherProvider.io) { getLabelsUseCase() }
        }
    }

    fun updateTaskFinished(id: Int, isFinished: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFinishedUseCase.invoke(id, isFinished)
                getFilteredTasks()
            }
        }
    }

    fun updateTaskFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFavouriteUseCase.invoke(id, isFavourite)
                getFilteredTasks()
            }
        }
    }
}