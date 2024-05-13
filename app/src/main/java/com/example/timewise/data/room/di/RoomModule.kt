package com.example.timewise.data.room.di

import android.content.Context
import androidx.room.Room
import com.example.timewise.data.LabelRepositoryImpl
import com.example.timewise.data.TaskRepositoryImpl
import com.example.timewise.data.room.DataBase
import com.example.timewise.data.room.dao.LabelDao
import com.example.timewise.data.room.dao.TaskDao
import com.example.timewise.domain.LabelRepository
import com.example.timewise.domain.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val ROOM_DATABASE_NAME = "room_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, DataBase::class.java, ROOM_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideLabelDao(db: DataBase) = db.loadLabelDao()

    @Singleton
    @Provides
    fun provideTaskDao(db: DataBase) = db.loadTaskDao()

    @Provides
    fun provideLabelRepository(labelDao: LabelDao): LabelRepository {
        return LabelRepositoryImpl(labelDao)
    }

    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(taskDao)
    }
}