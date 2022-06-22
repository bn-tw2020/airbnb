package com.example.todo.airbnb.di

import com.example.todo.airbnb.data.repository.DateRepositoryImpl
import com.example.todo.airbnb.data.repository.MainRepositoryImpl
import com.example.todo.airbnb.domain.repository.DateRepository
import com.example.todo.airbnb.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(): MainRepository {
        return MainRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideDateRepository(): DateRepository {
        return DateRepositoryImpl()
    }
}