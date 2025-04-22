
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
  CoroutineScope(coroutineContext).launch {
    try {
      val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("exceptionHandler: $throwable")
      }
      coroutineScope {
        val child = launch {
          try {
            println("The child is sleeping")
            delay(Long.MAX_VALUE)
          } finally {
            println("The child is cancelled")
          }
        }
        // Give our child a chance to execute and print using yield
        yield()
        println("Throwing an exception from the scope")
        throw AssertionError()
      }
    } catch (e: AssertionError) {
      println("Caught an assertion error")
    }
  }
}
