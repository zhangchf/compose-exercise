package com.zcf.composeexercise.todos.di

import com.zcf.composeexercise.todos.web.TodoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Retrofit {

  @Provides
  @Singleton
  fun providesTodoService(): TodoService {
    return Retrofit.Builder()
      .baseUrl(TodoService.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(TodoService::class.java)
  }
}