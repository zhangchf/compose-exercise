package com.zcf.composeexercise.pages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zcf.composeexercise.R
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.ui.theme.TextStyles
import com.zcf.composeexercise.widget.AliasOutlinedButton
import kotlinx.coroutines.launch

private enum class PageState {
  Scaffold1, Scaffold2
}

@Composable
fun BottomSheetScaffoldPage() {
  val scope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState()
  var pageState by remember { mutableStateOf(PageState.Scaffold1) }

  BackHandler(scaffoldState.bottomSheetState.isExpanded) {
    scope.launch {
      scaffoldState.bottomSheetState.collapse()
    }
  }

  BottomSheetScaffold(
    sheetContent = {
      BottomSheetContent(pageState)
    },
    scaffoldState = scaffoldState,
    sheetGesturesEnabled = true,
    sheetPeekHeight = 0.dp
  ) {
    Column {
      Text(
        text = "Main page content"
      )

      Button(
        onClick = {
          pageState = PageState.Scaffold1
          scope.launch {
            scaffoldState.bottomSheetState.expand()
          }
        }
      ) {
        Text(text = "Open Scaffold 1")
      }

      Button(
        onClick = {
          pageState = PageState.Scaffold2
          scope.launch {
            scaffoldState.bottomSheetState.expand()
          }
        }
      ) {
        Text(text = "Open Scaffold 2")
      }
    }
  }
}

@Composable
private fun ColumnScope.BottomSheetContent(
  pageState: PageState
) {
  when (pageState) {
    PageState.Scaffold1 -> {
//      PageState1BottomSheet()

      var consignItemsState by remember(consignItems) {
        println("zcf consignItemsState init")
        mutableStateOf(consignItems)
      }
      AddItemsToConsign(consignItemsState) { size, newCount ->
        println("zcf onCountChange, size: $size, newCount: $newCount")
        consignItems = consignItems.map {
          if (it.size == size) it.copy(count = newCount) else it
        }
        consignItemsState = consignItems
      }
    }

    PageState.Scaffold2 -> PageState2BottomSheet()
  }
}

@Composable
private fun PageState1BottomSheet() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Colors.gray2Solid)
  ) {
    Text("Top Line")

    Box(
      modifier = Modifier.weight(1f),
      contentAlignment = Alignment.Center
    ) {
      Text(text = "Middle content")
    }

    Text("Bottom Line")
  }
}

@Composable
private fun PageState2BottomSheet() {
  Column(
    modifier = Modifier.background(Colors.gray1Solid)
  ) {
    Text("Top Line 2")

    Box(
      modifier = Modifier.height(200.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(text = "Middle content 2")
    }

    Text("Bottom Line 2")
  }
}

data class ItemInfo(
  val size: String,
  val count: Int,
  val price: String
)

var consignItems = listOf(
  ItemInfo("10", 3, "$300"),
  ItemInfo("11", 4, "$300"),
  ItemInfo("11.5", 5, "$300"),
  ItemInfo("12", 100, "$300"),
  ItemInfo("13", 200, "$300"),
  ItemInfo("14", 3, "$300"),
  ItemInfo("15", 4, "$300"),
  ItemInfo("16", 5, "$300"),
  ItemInfo("17", 100, "$300"),
  ItemInfo("18", 200, "$300")
)

@Composable
private fun ColumnScope.AddItemsToConsign(
  consignItems: List<ItemInfo>,
  onConsignCountChange: (size: String, count: Int) -> Unit
) {
  BottomSheetDragHandle()

  Column(
    modifier = Modifier.padding(horizontal = 20.dp)
  ) {
    Text(
      text = "Add From Your Inventory",
      style = TextStyles.h2Medium(Colors.gray1Solid)
    )
    Spacer(Modifier.height(16.dp))
    Text(
      text = "By selecting sizes to add for the event you are accepting the event price. All items must be new and unworn. We will only be accepting a certain amount of slots.\n" +
        "\n" +
        "We also auto-populated the sizes we found in your inventory. Manage the sizes added. Learn More",
      style = TextStyles.bodyRegular(Colors.gray1Solid)
    )
  }

  println("zcf AddItemsToConsign, consignItems: $consignItems")

  Spacer(Modifier.height(24.dp))
  ConsignItemDivider()
  LazyColumn(
    modifier = Modifier.weight(1f)
  ) {
    println("zcf LazyColumn consignItems: $consignItems")
    consignItems.forEach { consignItem ->
      item {
        ConsignItem(consignItem) { newCount ->
          onConsignCountChange(consignItem.size, newCount)
        }
        ConsignItemDivider()
      }
    }
  }

  AliasOutlinedButton(
    modifier = Modifier
      .padding(horizontal = 20.dp)
      .padding(top = 16.dp, bottom = 24.dp),
    title = "Add"
  ) {
  }
}

@Composable
private fun ConsignItem(
  itemInfo: ItemInfo,
  onCountChange: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .height(72.dp)
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      modifier = Modifier.weight(1f),
      text = itemInfo.size,
      textAlign = TextAlign.Start
    )

    var count by remember { mutableIntStateOf(itemInfo.count) }

    val textFieldValue by remember {
      derivedStateOf {
        val countStr = count.toString()
        TextFieldValue(countStr, TextRange(countStr.length))
      }
    }

    fun setCount(value: Int) {
      count = value.coerceIn(0, 999)
      onCountChange(count)
    }

    Row(
      modifier = Modifier
        .fillMaxHeight()
        .border(width = 1.dp, color = Colors.darkGray),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        modifier = Modifier.clickable {
          setCount(count - 1)
        },
        painter = painterResource(R.drawable.ic_minus_24dp),
        contentDescription = "count minus 1"
      )
      VerticalDivider(color = Colors.darkGray)

      Box(
        modifier = Modifier.width(64.dp),
        contentAlignment = Alignment.Center
      ) {
        BasicTextField(
          modifier = Modifier.fillMaxWidth(),
          value = textFieldValue,
          onValueChange = { value ->
            val newCount = value.text.filter { it.isDigit() }.take(3).toIntOrNull() ?: 0
            setCount(newCount)
          },
          textStyle = TextStyle(textAlign = TextAlign.Center),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          cursorBrush = SolidColor(Colors.primary)
        )
      }

      VerticalDivider(color = Colors.darkGray)
      Image(
        modifier = Modifier.clickable {
          setCount(count + 1)
        },
        painter = painterResource(R.drawable.ic_plus_24dp),
        contentDescription = "count plus 1"
      )
    }

    Text(
      modifier = Modifier.weight(1f),
      text = itemInfo.price,
      textAlign = TextAlign.End
    )
  }
}

@Composable
private fun ConsignItemDivider() = Divider(color = Colors.darkGray)

@Composable
fun ColumnScope.BottomSheetDragHandle() {
  Box(
    modifier = Modifier
      .padding(vertical = 8.dp)
      .width(50.dp)
      .height(3.dp)
      .background(
        color = Colors.gray2Solid,
        shape = RoundedCornerShape(percent = 50)
      )
      .align(Alignment.CenterHorizontally)
  )
}

@Composable
fun BottomButton(
  text: String,
  onClick: () -> Unit
) {
}
