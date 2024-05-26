package com.example.timewise.ui.activities.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.core.DispatcherProvider
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.domain.usecase.label.GetLabelIdUseCase
import com.example.timewise.domain.usecase.task.DeleteTaskUseCase
import com.example.timewise.domain.usecase.task.GetTaskIdUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getLabelIdUseCase: GetLabelIdUseCase,
    private val getTaskIdUseCase: GetTaskIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskFinishedUseCase: UpdateTaskFinishedUseCase,
    private val updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase
) : ViewModel() {

    private var _label = MutableStateFlow<LabelModel?>(null)
    var label: StateFlow<LabelModel?> = _label

    private val _task = MutableStateFlow<TaskModel?>(null)
    val task: StateFlow<TaskModel?> = _task

    fun getLabelID(id: Int) {
        viewModelScope.launch {
            _label.value = withContext(dispatcherProvider.io) { getLabelIdUseCase(id) }
        }
    }

    fun getTaskID(id: Int) {
        viewModelScope.launch {
            _task.value = withContext(dispatcherProvider.io) { getTaskIdUseCase(id) }
        }
    }

    fun updateTask(task: TaskModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskUseCase.invoke(task)
                getTaskID(task.id)
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                deleteTaskUseCase.invoke(task)
            }
        }
    }

    fun updateTaskFinished(id: Int, isFinished: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFinishedUseCase.invoke(id, isFinished)
                getTaskID(id)
            }
        }
    }

    fun updateTaskFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFavouriteUseCase.invoke(id, isFavourite)
                getTaskID(id)
            }
        }
    }
}