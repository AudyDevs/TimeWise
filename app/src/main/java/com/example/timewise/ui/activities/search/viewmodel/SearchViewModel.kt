package com.example.timewise.ui.activities.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.core.DispatcherProvider
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.domain.usecase.label.GetLabelsUseCase
import com.example.timewise.domain.usecase.search.GetSearchedTasksUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getSearchedTasksUseCase: GetSearchedTasksUseCase,
    private val getLabelsUseCase: GetLabelsUseCase,
    private val updateTaskFinishedUseCase: UpdateTaskFinishedUseCase,
    private val updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase
) : ViewModel() {

    private var _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    var tasks: StateFlow<List<TaskModel>> = _tasks

    private var _labels = MutableStateFlow<List<LabelModel>>(emptyList())
    var labels: StateFlow<List<LabelModel>> = _labels

    var search: String = ""

    fun getSearchedTasks() {
        viewModelScope.launch {
            _tasks.value = withContext(dispatcherProvider.io) { getSearchedTasksUseCase(search) }
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
                getSearchedTasks()
            }
        }
    }

    fun updateTaskFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFavouriteUseCase.invoke(id, isFavourite)
                getSearchedTasks()
            }
        }
    }
}