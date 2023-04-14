package com.zcf.composeexercise.pages

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcf.composeexercise.ui.theme.Colors
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PriceAnalyticsView() {

    val tabs = listOf<String>("New New New", "New With Defects", "Used Used Used")

    val pagerState = rememberPagerState()
    val tabLazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        println("zcf1 launchedEffect")
        tabLazyListState.animateScrollToItem(pagerState.currentPage)
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


        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            state = tabLazyListState
        ) {
            items(tabs.size) { page ->
                val tabName = tabs[page]
                val (bgColor, fgColor) = if (selectedTabIndex == page) {
                    Colors.gray3 to Colors.background
                } else {
                    Colors.darkGray to Colors.gray1Solid
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .background(bgColor)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.scrollToPage(page)
                            }
                        }
                ) {
                    Text(
                        text = tabName,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        color = fgColor
                    )
                }
            }
        }

        HorizontalPager(
            pageCount = 3,
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            beyondBoundsPageCount = 1
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


@Composable
fun NestedScrollablePriceView(sizeInfoList: List<PdpSellSizeInfo>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .nestedScroll(connection = remember {object : NestedScrollConnection {}})
            .horizontalScroll(rememberScrollState())
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
