package com.zcf.composeexercise.todos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zcf.composeexercise.todos.model.Todo
import com.zcf.composeexercise.todos.web.TodoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
  private val todoService: TodoService
) : ViewModel() {

  val _todos = mutableStateListOf<Todo>()
  val todos: List<Todo> = _todos

  val _errorMsg = mutableStateOf<String>("")
  val errorMsg: State<String> = _errorMsg

  fun getTodos() {
    viewModelScope.launch {
      try {
        val newTodos = todoService.getTodos()
        _todos.clear()
        _todos.addAll(newTodos)
      } catch (e: Exception) {
        _errorMsg.value = "Get Todos failed: ${e.message}"
      }
    }
  }
}
