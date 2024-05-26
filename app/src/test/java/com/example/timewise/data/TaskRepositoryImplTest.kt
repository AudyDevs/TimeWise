package com.example.timewise.data

import com.example.timewise.core.extensions.map.toDomain
import com.example.timewise.core.extensions.map.toRoom
import com.example.timewise.data.room.dao.TaskDao
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FAVOURITE_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_FINISHED_STATE_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_LABEL
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_ID_TASK
import com.example.timewise.motherobject.TimeWiseMotherObject.ANY_SEARCHED_TASKS
import com.example.timewise.motherobject.TimeWiseMotherObject.anyListOfTaskEntity
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskEntity
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTaskModel
import com.example.timewise.motherobject.TimeWiseMotherObject.anyTodayFilteredTasks
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplTest {

    @MockK
    private lateinit var taskDao: TaskDao
    private lateinit var taskRepositoryImpl: TaskRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        taskRepositoryImpl = TaskRepositoryImpl(taskDao)
    }

    @Test
    fun `when TaskRepositoryImpl call getTasks successfully, it should return the tasks`() =
        runBlocking {
            //Given
            val anyTasksModel = anyListOfTaskEntity.map { it.toDomain() }
            coEvery { taskDao.getLabelTasks(ANY_ID_TASK) } returns anyListOfTaskEntity

            //When
            val tasksModelRepository = taskRepositoryImpl.getTasks(ANY_ID_TASK)

            //Then
            assert(tasksModelRepository == anyTasksModel)
        }

    @Test
    fun `when TaskRepositoryImpl call getTasksId successfully, it should return a correct TaskModel`() =
        runBlocking {
            //Given
            val anyTaskModel = anyTaskEntity.toDomain()
            coEvery { taskDao.getTaskID(ANY_ID_TASK) } returns anyTaskEntity

            //When
            val taskModelRepository = taskRepositoryImpl.getTasksId(ANY_ID_TASK)

            //Then
            assert(taskModelRepository == anyTaskModel)
        }

    @Test
    fun `when TaskRepositoryImpl call insertTask successfully, it should call insertTask from TaskDao once`() =
        runBlocking {
            //Given
            val taskEntity = anyTaskModel.toRoom()
            coEvery { taskDao.insertTask(taskEntity) } just runs

            //When
            taskRepositoryImpl.insertTask(anyTaskModel)

            //Then
            coVerify(exactly = 1) { taskDao.insertTask(taskEntity) }
        }

    @Test
    fun `when TaskRepositoryImpl call updateTask successfully, it should call updateLabel from TaskDao once`() =
        runBlocking {
            //Given
            val taskEntity = anyTaskModel.toRoom()
            coEvery { taskDao.updateTask(taskEntity) } just runs

            //When
            taskRepositoryImpl.updateTask(anyTaskModel)

            //Then
            coVerify(exactly = 1) { taskDao.updateTask(taskEntity) }
        }

    @Test
    fun `when TaskRepositoryImpl call updateTaskFinished successfully, it should call updateTaskFinished from TaskDao once`() =
        runBlocking {
            //Given
            coEvery { taskDao.updateTaskFinished(ANY_ID_TASK, ANY_FINISHED_STATE_TASK) } just runs

            //When
            taskRepositoryImpl.updateTaskFinished(ANY_ID_TASK, ANY_FINISHED_STATE_TASK)

            //Then
            coVerify(exactly = 1) {
                taskDao.updateTaskFinished(
                    ANY_ID_TASK,
                    ANY_FINISHED_STATE_TASK
                )
            }
        }

    @Test
    fun `when TaskRepositoryImpl call updateTaskFavourite successfully, it should call updateTaskFavourite from TaskDao once`() =
        runBlocking {
            //Given
            coEvery { taskDao.updateTaskFavourite(ANY_ID_TASK, ANY_FAVOURITE_STATE_TASK) } just runs

            //When
            taskRepositoryImpl.updateTaskFavourite(ANY_ID_TASK, ANY_FAVOURITE_STATE_TASK)

            //Then
            coVerify(exactly = 1) {
                taskDao.updateTaskFavourite(
                    ANY_ID_TASK,
                    ANY_FAVOURITE_STATE_TASK
                )
            }
        }

    @Test
    fun `when TaskRepositoryImpl call deleteTask successfully, it should call deleteTask from TaskDao once`() =
        runBlocking {
            //Given
            val taskEntity = anyTaskModel.toRoom()
            coEvery { taskDao.deleteTask(taskEntity) } just runs

            //When
            taskRepositoryImpl.deleteTask(anyTaskModel)

            //Then
            coVerify(exactly = 1) { taskDao.deleteTask(taskEntity) }
        }

    @Test
    fun `when TaskRepositoryImpl call deleteAllTasks successfully, it should call deleteAllTasks from TaskDao once`() =
        runBlocking {
            //Given
            coEvery { taskDao.deleteAllTasks(ANY_ID_LABEL) } just runs

            //When
            taskRepositoryImpl.deleteAllTasks(ANY_ID_LABEL)

            //Then
            coVerify(exactly = 1) { taskDao.deleteAllTasks(ANY_ID_LABEL) }
        }

    @Test
    fun `when TaskRepositoryImpl call getNumberFilteredTasks successfully, it should call getAllTasks from TaskDao once`() =
        runBlocking {
            //Given
            coEvery { taskDao.getAllTasks() } returns anyListOfTaskEntity

            //When
            taskRepositoryImpl.getNumberFilteredTasks(anyTodayFilteredTasks)

            //Then
            coVerify(exactly = 1) { taskDao.getAllTasks() }
        }

    @Test
    fun `when TaskRepositoryImpl call getFilteredTasks successfully, it should call getAllTasks from TaskDao once and return the filtered tasks`() =
        runBlocking {
            //Given
            val anyTasksModel = anyListOfTaskEntity.map { it.toDomain() }
            coEvery { taskDao.getAllTasks() } returns anyListOfTaskEntity

            //When
            val tasksModelFiltered = taskRepositoryImpl.getFilteredTasks(anyTodayFilteredTasks)

            //Then
            coVerify(exactly = 1) { taskDao.getAllTasks() }
            assert(tasksModelFiltered == anyTasksModel)
        }

    @Test
    fun `when TaskRepositoryImpl call getSearchedTasks successfully, it should call getAllTasks from TaskDao once and return the searched tasks`() =
        runBlocking {
            //Given
            coEvery { taskDao.getAllTasks() } returns anyListOfTaskEntity

            //When
            val tasksModelSearched = taskRepositoryImpl.getSearchedTasks(ANY_SEARCHED_TASKS)
            val responseFiltered =
                anyListOfTaskEntity.filter {
                    it.name.uppercase().contains(ANY_SEARCHED_TASKS.uppercase())
                }
            responseFiltered.map { it.toDomain() }

            //Then
            coVerify(exactly = 1) { taskDao.getAllTasks() }
            assert(tasksModelSearched == responseFiltered)
        }
}

