package com.example.timewise.ui.home

import androidx.lifecycle.ViewModel
import com.example.timewise.data.providers.HomeProvider
import com.example.timewise.domain.model.LabelModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(homeProvider: HomeProvider) : ViewModel() {

    private var _tasks = MutableStateFlow<List<LabelModel>>(emptyList())
    var tasks: StateFlow<List<LabelModel>> = _tasks

    init {
        _tasks.value = homeProvider.getLabels()
    }
}