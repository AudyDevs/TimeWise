package com.example.timewise.ui.activities.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.domain.usecase.task.DeleteTaskUseCase
import com.example.timewise.domain.usecase.task.GetTaskIdUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val getTaskIdUseCase: GetTaskIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskFinishedUseCase: UpdateTaskFinishedUseCase,
    private val updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase
) : ViewModel() {

    private val _task = MutableStateFlow<TaskModel?>(null)
    val task: StateFlow<TaskModel?> = _task

    fun getTaskID(id: Int, idLabel: Int) {
        viewModelScope.launch {
            _task.value = withContext(Dispatchers.IO) { getTaskIdUseCase(id, idLabel) }
        }
    }

    fun updateTask(task: TaskModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTaskUseCase.invoke(task)
                getTaskID(task.id, task.idLabel)
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteTaskUseCase.invoke(task)
            }
        }
    }

    fun updateTaskFinished(id: Int, idLabel: Int, isFinished: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTaskFinishedUseCase.invoke(id, idLabel, isFinished)
                getTaskID(id, idLabel)
            }
        }
    }

    fun updateTaskFavourite(id: Int, idLabel: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTaskFavouriteUseCase.invoke(id, idLabel, isFavourite)
                getTaskID(id, idLabel)
            }
        }
    }
}