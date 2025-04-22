package com.zcf.experiment

import junit.framework.TestCase.assertTrue
import org.junit.Test

class DataClassTest {

  data class MutableMapModel(
    val map: MutableMap<String, Boolean> = mutableMapOf()
  )
  data class ImmutableMapModel(
    val map: Map<String, Boolean> = emptyMap()
  )

  @Test
  fun `mutableMap in data class`() {
    val model = MutableMapModel()
    model.map.put("test", true)
    val model2 = model
    assertTrue(model == model2)
  }

  @Test
  fun `ImmutableMap in data class`() {
    val model = ImmutableMapModel()
    val map = model.map.toMutableMap()
    map["test"] = true
    val model2 = model.copy(map = map)
    assertTrue(model != model2)
  }
}
