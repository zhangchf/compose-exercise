package com.zcf.experiment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Execution context of Kotlin Flows:
 * https://elizarov.medium.com/execution-context-of-kotlin-flows-b8c151c9309b
 */
class FlowInvariantTest {

    private fun dataFlow_changeContextWhenEmit_violateFlowInvariant(expectedResult: Int): Flow<Int> {
        fun someDataComputation(): Int = expectedResult

        return flow { // create emitter
            withContext(Dispatchers.Default) {
                emit(someDataComputation())
            }
        }
    }

    private fun dataFlow_flowOn_complyWithFlowInvariant(expectedResult: Int): Flow<Int> {
        fun someDataComputation(): Int = expectedResult

        return flow { // create emitter
            emit(someDataComputation())
        }
            .flowOn(Dispatchers.Default)
    }

    private fun dataFlow_changeContextInPlace_complyWithFlowInvariant(expectedResult: Int): Flow<Int> {
        suspend fun someDataComputation(): Int {
            return withContext(Dispatchers.Default) {
                expectedResult
            }
        }

        return flow { // create emitter
            emit(someDataComputation())
        }
    }

    @Test
    fun `Violated flow context invariant policy if change context when emit`() = runTest {
        try {
            dataFlow_changeContextWhenEmit_violateFlowInvariant(10000).collect {
                println("zcf $it")
            }
        } catch (e: Exception) {
            assertTrue(e is IllegalStateException)
            assertTrue(e.message!!.contains("Flow invariant is violated"))
        }
    }

    @Test
    fun `Good to change flow context with flowOn operator`() = runTest {
        val expectedResult = 10000
        dataFlow_flowOn_complyWithFlowInvariant(expectedResult).collect {
            assertTrue(it == expectedResult)
        }
    }

    @Test
    fun `Good to change flow context in the data processing point`() = runTest {
        val expectedResult = 10000
        dataFlow_changeContextInPlace_complyWithFlowInvariant(expectedResult).collect {
            assertTrue(it == expectedResult)
        }
    }
}