package com.example.timewise.motherobject

import com.example.timewise.core.FilterTypes
import com.example.timewise.core.Time
import com.example.timewise.data.room.entities.LabelEntity
import com.example.timewise.data.room.entities.TaskEntity
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import java.util.Date

object TimeWiseMotherObject {

    private val anyDate: Date = Time.currentDate()

    val anyLabelModel = LabelModel(1, 1, "LabelNameTest", 1, 1)
    val anyLabelEntity = LabelEntity(1, 1, "LabelNameTest", 1, 1)
    val anyTaskModel =
        TaskModel(
            1,
            1,
            "TaskNameTest",
            false,
            anyDate,
            false,
            anyDate,
            anyDate,
            "TaskDetailTest",
            anyDate
        )
    val anyTaskEntity =
        TaskEntity(
            1,
            1,
            "TaskNameTest",
            false,
            anyDate,
            false,
            anyDate,
            anyDate,
            "TaskDetailTest",
            anyDate
        )

    val anyListOfLabelModel = listOf(
        LabelModel(1, 1, "LabelName1Test", 1, 1),
        LabelModel(2, 2, "LabelName2Test", 2, 2),
        LabelModel(3, 3, "LabelName3Test", 3, 3),
        LabelModel(4, 4, "LabelName4Test", 4, 4)
    )

    val anyListOfTaskModel = listOf(
        TaskModel(
            1,
            1,
            "TaskName1Test",
            false,
            anyDate,
            false,
            anyDate,
            anyDate,
            "TaskDetail1Test",
            anyDate
        ),
        TaskModel(
            2,
            2,
            "TaskName2Test",
            false,
            anyDate,
            false,
            anyDate,
            anyDate,
            "TaskDetail2Test",
            anyDate
        ),
        TaskModel(
            3,
            3,
            "TaskName3Test",
            false,
            anyDate,
            false,
            anyDate,
            anyDate,
            "TaskDetail3Test",
            anyDate
        ),
        TaskModel(
            4,
            4,
            "TaskName4Test",
            false,
            anyDate,
            false,
            anyDate,
            anyDate,
            "TaskDetail4Test",
            anyDate
        )
    )
    const val ANY_NUMBER_OF_FILTERED_TASKS = 1

    val anyTodayFilteredTasks = FilterTypes.Today.type
    val anyWeekFilteredTasks = FilterTypes.Week.type
    val anyLaterFilteredTasks = FilterTypes.Later.type
    val anyExpiredFilteredTasks = FilterTypes.Expired.type

    const val ANY_ID_LABEL = 1
    const val ANY_ID_TASK = 1
    const val ANY_FINISHED_STATE_TASK = true
    const val ANY_FAVOURITE_STATE_TASK = true
    const val ANY_SEARCHED_TASKS = "searchTest"
}