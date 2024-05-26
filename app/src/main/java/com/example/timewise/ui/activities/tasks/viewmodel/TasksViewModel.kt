package com.example.timewise.ui.activities.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timewise.core.DispatcherProvider
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.domain.usecase.label.DeleteLabelUseCase
import com.example.timewise.domain.usecase.label.GetLabelIdUseCase
import com.example.timewise.domain.usecase.label.UpdateLabelUseCase
import com.example.timewise.domain.usecase.task.DeleteAllTasksUseCase
import com.example.timewise.domain.usecase.task.GetTasksUseCase
import com.example.timewise.domain.usecase.task.InsertTaskUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFavouriteUseCase
import com.example.timewise.domain.usecase.task.UpdateTaskFinishedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getLabelIdUseCase: GetLabelIdUseCase,
    private val updateLabelUseCase: UpdateLabelUseCase,
    private val deleteLabelUseCase: DeleteLabelUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskFinishedUseCase: UpdateTaskFinishedUseCase,
    private val updateTaskFavouriteUseCase: UpdateTaskFavouriteUseCase,
    private val deleteAllTasksUseCase: DeleteAllTasksUseCase
) : ViewModel() {

    private var _label = MutableStateFlow<LabelModel?>(null)
    var label: StateFlow<LabelModel?> = _label

    private var _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    var tasks: StateFlow<List<TaskModel>> = _tasks

    var idLabel = 0

    fun getLabelID(id: Int) {
        viewModelScope.launch {
            _label.value = withContext(dispatcherProvider.io) { getLabelIdUseCase(id) }
        }
    }

    fun updateLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateLabelUseCase.invoke(label)
                getLabelID(label.id)
            }
        }
    }

    fun deleteLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                deleteLabelUseCase.invoke(label)
                deleteAllTasksUseCase.invoke(label.id)
            }
        }
    }

    fun getTasks() {
        viewModelScope.launch {
            _tasks.value = withContext(dispatcherProvider.io) { getTasksUseCase(idLabel) }
        }
    }

    fun insertTask(task: TaskModel) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                insertTaskUseCase.invoke(task)
                getTasks()
            }
        }
    }

    fun updateTaskFinished(id: Int, isFinished: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFinishedUseCase.invoke(id, isFinished)
                getTasks()
            }
        }
    }

    fun updateTaskFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            withContext(dispatcherProvider.io) {
                updateTaskFavouriteUseCase.invoke(id, isFavourite)
                getTasks()
            }
        }
    }
}