package com.zcf.composeexercise.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.IntrinsicSize.Max
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcf.composeexercise.ui.theme.Colors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PriceAnalyticsView() {
  val tabs = listOf<String>("New New New New", "New With Defects", "Used Used Used Used")

  val pagerState = rememberPagerState { tabs.size }
  val tabLazyListState = rememberLazyListState()
  val tabRowState = rememberScrollState()

  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(pagerState.currentPage) {
    println("zcf1 launchedEffect")
    tabLazyListState.animateScrollToItem(pagerState.currentPage)

    println("zcf1, tabRowState, value: ${tabRowState.value}, maxValue: ${tabRowState.maxValue}")
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = Colors.background)
  ) {
    val selectedTabIndex = pagerState.currentPage

    ScrollableTabRow(
      selectedTabIndex = selectedTabIndex,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .align(Alignment.Start),
      divider = {}
    ) {
      tabs.forEachIndexed { page, tabName ->
        Tab(
          selected = page == selectedTabIndex,
          onClick = {
            coroutineScope.launch {
              pagerState.scrollToPage(page)
            }
          }
        ) {
          Text(
            text = tabName,
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp
          )
        }
      }
    }

    AliasScrollableTab_LazyRow(
      tabs = tabs,
      selectedTabIndex = selectedTabIndex
    ) { page ->
      coroutineScope.launch {
        pagerState.scrollToPage(page)
      }
    }

    AliasScrollableTab_CustomizeRow(
      tabs = tabs,
      selectedTabIndex = selectedTabIndex
    ) { page ->
      coroutineScope.launch {
        pagerState.scrollToPage(page)
      }
    }

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState
    ) { page ->

      val sizeInfoList = mockPdpSellState

      Box(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .width(Max)
          ) {
            PriceItemDivider()
            PriceHeaderText(text = "US M")
            sizeInfoList.forEach {
              PriceItemDivider()
              PriceItemText(text = it.size)
            }
            PriceItemDivider()
          }
          Column(
            modifier = Modifier
              .weight(1f)
              .background(
                brush = Brush.horizontalGradient(
                  colorStops = arrayOf(
                    Pair(0f, Color.Black),
                    Pair(0.1f, Colors.windowBackground),
                    Pair(0.9f, Colors.windowBackground),
                    Pair(1.0f, Color.Black)
                  )
                )
              )
          ) {
            NestedScrollablePriceView(sizeInfoList = sizeInfoList)
          }
          Column(
            modifier = Modifier
              .width(100.dp)
          ) {
            PriceItemDivider()
            PriceHeaderText(text = "TOP OFFER")
            sizeInfoList.forEach {
              PriceItemDivider()
              PriceItemButton(text = it.topOffer)
            }
            PriceItemDivider()
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NestedScrollablePriceView(sizeInfoList: List<PdpSellSizeInfo>) {
  val scrollState = rememberScrollState()

  val coroutineScope = rememberCoroutineScope()

  val overscrollEffect = ScrollableDefaults.overscrollEffect()

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState)
      .handleNestedHorizontalScroll(scrollState, overscrollEffect, coroutineScope)
  ) {
    Column(
      modifier = Modifier.width(Max)
    ) {
      PriceItemDivider()
      PriceHeaderText(text = "LAST")
      sizeInfoList.forEach {
        PriceItemDivider()
        PriceItemText(text = it.lastSoldPrice ?: randomText())
      }
      PriceItemDivider()
    }
    Column(
      modifier = Modifier.width(Max)
    ) {
      PriceItemDivider()
      PriceHeaderText(text = "LOWEST")
      sizeInfoList.forEach {
        PriceItemDivider()
        PriceItemText(text = it.lowestPrice ?: randomText())
      }
      PriceItemDivider()
    }
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
        println("zcf1 onDragStarted, canScrollBackward: $canScrollBackward, canScrollForward: $canScrollForward")

        fun onHorizontalDrag(delta: Float) {
          overscrollEffect.applyToScroll(Offset(delta, 0f), NestedScrollSource.Drag) {
            val consumed = when {
              canScrollBackward && delta > 0 -> scrollState.value.coerceAtMost(delta.roundToInt())
              canScrollForward && delta < 0 -> (scrollState.value - scrollState.maxValue).coerceAtLeast(
                delta.roundToInt()
              )
              else -> 0
            }.toFloat()
            println("zcf1 onHorizontalDrag, delta: $delta, consumed: $consumed")
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
fun NestedScrollablePriceView_nestedScroll(sizeInfoList: List<PdpSellSizeInfo>) {
  val childScrollState = rememberScrollState()

  var canScrollBackward by remember { mutableStateOf(false) }
  var canScrollForward by remember { mutableStateOf(false) }

  val nestedScrollDispatcher = remember { NestedScrollDispatcher() }
  val nestedScrollConnection = remember {
    object : NestedScrollConnection {
      override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        println("zcf1 onPostFling, consumed: $consumed, available: $available")
        return super.onPostFling(consumed, available)
      }

      override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
      ): Offset {
        println("zcf1 onPostScroll, consumed: $consumed, available: $available")
        return super.onPostScroll(consumed, available, source)
      }

      override suspend fun onPreFling(available: Velocity): Velocity {
        println("zcf1 onPreFling, available: $available")
        return super.onPreFling(available)
      }

      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        println("zcf1 onPreScroll, available: $available")
//                if (!canScrollBackward && available.x < 0) {
//                    return available
//                }
//                if (!canScrollForward && available.x > 0) {
//                    return available
//                }
        return super.onPreScroll(available, source)
      }
    }
  }

  Box(
    modifier = Modifier
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(childScrollState)
        .nestedScroll(
          connection = nestedScrollConnection,
          dispatcher = nestedScrollDispatcher
        )
//                .pointerInput(Unit) {
// //                    detectHorizontalDragGestures()
//
//                }
        .draggable(
          orientation = Orientation.Horizontal,
          state = rememberDraggableState { delta ->
            println("zcf1 dragState, delta:$delta")
            nestedScrollDispatcher.dispatchPreScroll(
              available = Offset(delta, 0f),
              source = NestedScrollSource.Drag
            )
            val consumed = when {
              canScrollBackward && delta > 0 -> delta
              canScrollForward && delta < 0 -> delta
              else -> 0f
            }
            if (consumed != 0f) {
              nestedScrollDispatcher.coroutineScope.launch {
                childScrollState.scrollBy(-delta)
              }
            }
            nestedScrollDispatcher.dispatchPostScroll(
              consumed = Offset(consumed, 0f),
              available = Offset(delta - consumed, 0f),
              source = NestedScrollSource.Drag
            )
          },
          onDragStarted = {
            canScrollBackward = childScrollState.canScrollBackward
            canScrollForward = childScrollState.canScrollForward
            println("zcf1 onDragStarted, canScrollBackward: $canScrollBackward, canScrollForward: $canScrollForward")
          },
          onDragStopped = { velocityX ->
            val original = Velocity(velocityX, 0f)
            nestedScrollDispatcher.dispatchPostFling(
              consumed = Velocity.Zero,
              available = original
            )
          }
        )
    ) {
      Column(
        modifier = Modifier.width(Max)
      ) {
        PriceItemDivider()
        PriceHeaderText(text = "LAST")
        sizeInfoList.forEach {
          PriceItemDivider()
          PriceItemText(text = it.lastSoldPrice ?: randomText())
        }
        PriceItemDivider()
      }
      Column(
        modifier = Modifier.width(Max)
      ) {
        PriceItemDivider()
        PriceHeaderText(text = "LOWEST")
        sizeInfoList.forEach {
          PriceItemDivider()
          PriceItemText(text = it.lowestPrice ?: randomText())
        }
        PriceItemDivider()
      }
    }
  }
}

@Composable
fun AliasScrollableTab_CustomizeRow(
  tabs: List<String>,
  selectedTabIndex: Int,
  onTabSelected: (Int) -> Unit
) {
  val scrollState = rememberScrollState()

  val tabPositions = mutableMapOf<Int, Float>()

  LaunchedEffect(key1 = selectedTabIndex) {
    tabPositions[selectedTabIndex]?.let {
      println("zcf1 scroll to tab: $selectedTabIndex, $it")
      scrollState.animateScrollTo(it.roundToInt())
    }
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(scrollState)
      .padding(horizontal = 4.dp, vertical = 8.dp)
  ) {
    tabs.forEachIndexed { page, tabName ->

      val bgColorState by if (selectedTabIndex == page) {
        animateColorAsState(targetValue = Colors.gray3)
      } else {
        animateColorAsState(targetValue = Colors.darkGray)
      }

      val fgColorState by if (selectedTabIndex == page) {
        animateColorAsState(targetValue = Colors.background)
      } else {
        animateColorAsState(targetValue = Colors.gray1Solid)
      }

      Box(
        modifier = Modifier
          .onGloballyPositioned {
            tabPositions[page] = it.positionInParent().x
            println("zcf1 tabPositions: $page, ${tabPositions[page]}")
          }
          .padding(horizontal = 4.dp)
          .background(bgColorState)
          .clickable {
            onTabSelected(page)
          }
      ) {
        Text(
          text = tabName,
          modifier = Modifier.padding(8.dp),
          fontSize = 16.sp,
          color = fgColorState
        )
      }
    }
  }
}

@Composable
fun AliasScrollableTab_LazyRow(
  tabs: List<String>,
  selectedTabIndex: Int,
  onTabSelected: (Int) -> Unit
) {
  val scrollState = rememberLazyListState()

  LaunchedEffect(key1 = selectedTabIndex) {
    scrollState.animateScrollToItem(selectedTabIndex)
  }

  LazyRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp, vertical = 8.dp),
    state = scrollState
  ) {
    items(
      count = tabs.size,
      key = { index -> tabs[index] }
    ) { page ->
      val tabName = tabs[page]

      val bgColorState by if (selectedTabIndex == page) {
        animateColorAsState(targetValue = Colors.gray3)
      } else {
        animateColorAsState(targetValue = Colors.darkGray)
      }

      val fgColorState by if (selectedTabIndex == page) {
        animateColorAsState(targetValue = Colors.background)
      } else {
        animateColorAsState(targetValue = Colors.gray1Solid)
      }

      Box(
        modifier = Modifier
          .padding(horizontal = 4.dp)
          .background(bgColorState)
          .clickable {
            onTabSelected(page)
          }
      ) {
        Text(
          text = tabName,
          modifier = Modifier.padding(8.dp),
          fontSize = 16.sp,
          color = fgColorState
        )
      }
    }
  }
}

@Composable
fun PriceHeaderText(text: String?) = Text(
  text = text ?: "",
  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
  fontSize = 12.sp,
  maxLines = 1
)

@Composable
fun PriceItemText(text: String?) = Box(
  modifier = Modifier
    .height(80.dp)
    .fillMaxWidth()
    .padding(horizontal = 12.dp),
  contentAlignment = Alignment.CenterStart
) {
  Text(
    text = text ?: "",
    maxLines = 1,
    color = Colors.gray3
  )
}

@Composable
fun PriceItemButton(text: String?) = Box(
  modifier = Modifier
    .height(80.dp)
    .fillMaxWidth(),
  contentAlignment = Alignment.Center
) {
  if (text?.isNotBlank() == true) {
    Button(
      onClick = { },
      colors = ButtonDefaults.buttonColors(
        backgroundColor = Colors.positive
      )
    ) {
      Text(
        text = text ?: "",
        maxLines = 1
      )
    }
  }
}

@Composable
fun PriceItemDivider() = Divider(color = Color.DarkGray)

data class PdpSellSizeInfo(
  val size: String,
  val lastSoldPrice: String?,
  val lowestPrice: String?,
  val topOffer: String?,
  val lowestGlobalPrice: String?
)

private val mockPdpSellState = (1..20).map {
  PdpSellSizeInfo(
    size = Random.nextInt(3, 20).toString(),
    lastSoldPrice = randomText(),
    lowestPrice = randomText(),
    topOffer = randomTopOffer(),
    lowestGlobalPrice = null
  )
}

fun randomText() = if (Random.nextInt().mod(2) == 0) "EUR123456/EUR987654" else "$123456"

fun randomTopOffer() = if (Random.nextInt().mod(4) == 0) "$123456" else ""

@Preview
@Composable
fun PriceAnalyticsViewPreview() {
  PriceAnalyticsView()
}
