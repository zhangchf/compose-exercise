package com.zcf.experiment

data class Model(
  val name: String,
  val qrCode: ByteArray
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Model

    if (name != other.name) return false
    if (!qrCode.contentEquals(other.qrCode)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + qrCode.contentHashCode()
    return result
  }
}
