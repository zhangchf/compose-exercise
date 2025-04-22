package com.zcf.experiment

import org.junit.Assert.*
import org.junit.Test

class ModelWithWrappedPropertyTest {

  @Test
  fun `QrCode test equals`() {
    val qrCode1 = QrCode(byteArrayOf(1, 2, 3))
    val qrCode2 = QrCode(byteArrayOf(1, 2, 3))

    assertEquals(qrCode1, qrCode2)
  }

  @Test
  fun `QrCode test non equals`() {
    val qrCode1 = QrCode(byteArrayOf(1, 2, 3, 4))
    val qrCode2 = QrCode(byteArrayOf(1, 2, 3))

    assertNotEquals(qrCode1, qrCode2)

    val qrCode3 = QrCode(byteArrayOf(2, 2, 3))
    val qrCode4 = QrCode(byteArrayOf(1, 2, 3))

    assertNotEquals(qrCode3, qrCode4)
  }

  @Test
  fun `Model test equals`() {
    val model1 = ModelWithWrappedProperty("test", QrCode(byteArrayOf(1, 2, 3)))
    val model2 = ModelWithWrappedProperty("test", QrCode(byteArrayOf(1, 2, 3)))

    assertEquals(model1, model2)
  }

  @Test
  fun `Model test non equals`() {
    val model1 = ModelWithWrappedProperty("test", QrCode(byteArrayOf(1, 1, 3)))
    val model2 = ModelWithWrappedProperty("test", QrCode(byteArrayOf(1, 2, 3)))

    assertNotEquals(model1, model2)
  }
}
