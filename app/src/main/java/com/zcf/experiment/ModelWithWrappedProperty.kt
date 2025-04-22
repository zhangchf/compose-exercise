package com.zcf.experiment

data class QrCode(val data: ByteArray) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as QrCode

    if (!data.contentEquals(other.data)) return false

    return true
  }

  override fun hashCode(): Int {
    return data.contentHashCode()
  }
}

data class ModelWithWrappedProperty(
  val name: String,
  val qrCode: QrCode
)
