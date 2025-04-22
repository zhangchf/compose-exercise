package com.zcf.composeexercise.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

object Colors {
  val primary = Color(0xFF7856FF)
  val links = primary
  val links50 = Color(0x807856FF)
  val vacationMode = Color(0xFFA4A4A4)

  val error = Color(0xFFFF2E53)

  val windowBackground = Color(0xFF121212)
  val gray1 = Color(0x4DFFFFFF)
  val gray1Solid = Color(0xFF4D4D4D)
  val gray2 = Color(0x99FFFFFF)
  val gray2Solid = Color(0xFF7D7D7D)
  val gray3 = Color(0xDEFFFFFF)
  val gray3Solid = Color(0xFFF2F2F2)
  val darkGray = Color(0xFF272727)
  val darkGray50 = Color(0x80272727)

  val background = Color(0xFF111111)
  val background98 = Color(0xFA111111)
  val background80 = Color(0xCC111111)
  val background50 = Color(0x80111111)
  val transparentBackground = Color(0xF2111111)
  val transparentGray50 = Color(0x80C4C4C4)

  val dimBackground = Color(0x80000000)
  val black40 = Color(0x66000000)

  val ripple = Color(0x33FFFFFF)
  val rippleDark = Color(0x33121212)

  val positive = Color(0xFF56FFC2)
  val positive20 = Color(0x3356FFC2)
  val mild = Color(0xFF2E82FF)
  val negative = Color(0xFFFF2E53)

  val highlight = Color(0xFFD6FE36)
  val offBlack = Color(0xFF1A1A1A)

  val white = Color(0xFFFFFFFF)
  val flightClubRed = Color(0xFFE91C23)

  val materialColors = darkColors(
    primary = links,
    primaryVariant = Color.Black,
    secondary = links,
    background = background
  )
}
