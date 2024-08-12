package com.zcf.composeexercise.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.overscroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sign

fun Modifier.thenIf(
  condition: Boolean,
  ifTrue: Modifier,
  ifFalse: Modifier? = null
) = if (condition) {
  then(ifTrue)
} else if (ifFalse != null) {
  then(ifFalse)
} else {
  this
}

val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

@Composable
fun Modifier.dashedUnderline(color: Color): Modifier {
  return drawBehind {
    drawLine(
      color = color,
      start = Offset(0f, size.height),
      end = Offset(size.width, size.height),
      pathEffect = dashPathEffect
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.handleNestedHorizontalScroll(
  scrollState: ScrollState,
  overscrollEffect: OverscrollEffect,
  coroutineScope: CoroutineScope
): Modifier {
  return this
    .overscroll(overscrollEffect)
    .pointerInput(Unit) {
      awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        val canScrollBackward = scrollState.canScrollBackward
        val canScrollForward = scrollState.canScrollForward

        fun onHorizontalDrag(delta: Float) {
          overscrollEffect.applyToScroll(Offset(delta, 0f), NestedScrollSource.Drag) {
            val consumed = when {
              canScrollBackward && delta > 0 -> scrollState.value.coerceAtMost(delta.roundToInt())
              canScrollForward && delta < 0 -> (scrollState.value - scrollState.maxValue).coerceAtLeast(
                delta.roundToInt()
              )

              else -> 0
            }.toFloat()
            coroutineScope.launch {
              scrollState.scrollBy(-consumed)
            }
            Offset(consumed, 0f)
          }
        }

        fun onHorizontalDragEnd(velocity: Velocity) {
          coroutineScope.launch {
            overscrollEffect.applyToFling(velocity) {
              Velocity.Zero
            }
          }
        }

        var overSlop = 0f
        val drag = awaitHorizontalTouchSlopOrCancellation(down.id) { change, over ->
          val consume = when {
            overscrollEffect.isInProgress -> true
            canScrollBackward && over > 0 -> true
            canScrollForward && over < 0 -> true
            else -> false
          }
          if (consume) {
            change.consume()
            overSlop = over
          }
        }
        if (drag != null) {
          onHorizontalDrag(overSlop)
          if (horizontalDrag(drag.id) {
            onHorizontalDrag(it.positionChange().x)
            it.consume()
          }
          ) {
            // As we can't determine the end velocity here,
            // we just use a number which is adjusted to have a good UI effect
            onHorizontalDragEnd(Velocity(10f * overSlop.sign, 0f))
          }
        }
      }
    }
}

@Composable
fun Modifier.clickableWithRoundRipple(onClick: () -> Unit): Modifier {
  return this.clickable(
    interactionSource = remember {
      MutableInteractionSource()
    },
    indication = rememberRipple(bounded = false),
    onClick = onClick
  )
}
