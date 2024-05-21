package com.example.timewise.ui.activities.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getLabelID(id: Int) {
        viewModelScope.launch {
            _label.value = withContext(Dispatchers.IO) { getLabelIdUseCase(id) }
        }
    }

    fun updateLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateLabelUseCase.invoke(label)
                getLabelID(label.id)
            }
        }
    }

    fun deleteLabel(label: LabelModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteLabelUseCase.invoke(label)
                deleteAllTasksUseCase.invoke(label.id)
            }
        }
    }

    fun getTasks() {
        viewModelScope.launch {
            _tasks.value = withContext(Dispatchers.IO) { getTasksUseCase(label.value!!.id) }
        }
    }

    fun insertTask(task: TaskModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertTaskUseCase.invoke(task)
                getTasks()
            }
        }
    }

    fun updateTaskFinished(id: Int, isFinished: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTaskFinishedUseCase.invoke(id, isFinished)
                getTasks()
            }
        }
    }

    fun updateTaskFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTaskFavouriteUseCase.invoke(id, isFavourite)
                getTasks()
            }
        }
    }
}