package com.zcf.composeexercise.todos.web

import com.zcf.composeexercise.todos.model.Todo
import retrofit2.http.GET

interface TodoService {

  companion object {
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
  }

  @GET("/todos")
  suspend fun getTodos(): List<Todo>
}