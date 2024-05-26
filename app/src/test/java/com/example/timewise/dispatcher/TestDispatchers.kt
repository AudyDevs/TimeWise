package com.example.timewise.dispatcher

import com.example.timewise.core.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import javax.inject.Inject

class TestDispatchers @Inject constructor() : DispatcherProvider {
    val testDispatchers = StandardTestDispatcher()
    override val main: CoroutineDispatcher
        get() = testDispatchers
    override val io: CoroutineDispatcher
        get() = testDispatchers
    override val default: CoroutineDispatcher
        get() = testDispatchers
}