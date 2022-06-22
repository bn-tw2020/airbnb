package com.example.todo.airbnb.presentation.search.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.domain.model.Search
import java.text.DecimalFormat

@Composable
fun CustomDialog(
    dialogState: Boolean,
    onDismissRequest: (dialogState: Boolean) -> Unit,
    content: @Composable (Boolean) -> Unit,
) {
    if (dialogState) {
        Dialog(
            onDismissRequest = { onDismissRequest(dialogState) },
            properties = DialogProperties(dismissOnBackPress = false,
                dismissOnClickOutside = false),
        ) {
            Surface(
                modifier = Modifier
                    .width(343.dp)
                    .height(620.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color.White
            ) {
                content(dialogState)
            }
        }
    }
}

@Composable
fun DialogContent(
    dialogState: Boolean,
    accommodationResult: AccommodationResult,
    reservation: Search,
    onDismissRequest: (dialogState: Boolean) -> Unit,
    onOrder: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            modifier = Modifier
                .clickable { onDismissRequest(dialogState) }
                .align(Alignment.End)
        )
        CustomSpacer(modifier = Modifier.height(10.dp))
        Text(
            text = "₩${DecimalFormat("#,###").format(accommodationResult.fee)} / 박",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            fontSize = 16.sp,
            lineHeight = 17.sp
        )
        CustomSpacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .border(2.dp, Color.LightGray, RoundedCornerShape(10.dp))
        ) {
            Row {
                Column(
                    modifier = Modifier.padding(start = 10.dp,
                        top = 10.dp,
                        bottom = 10.dp,
                        end = 55.dp)
                ) {
                    Text(text = "체크인")
                    Text(text = reservation.checkIn.substring(6))
                }
                Spacer(modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(Color.LightGray))
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "체크아웃")
                    Text(text = reservation.checkOut.substring(6))
                }
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray))
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = "인원")
                Text(text = "게스트 ${reservation.guest}명")
            }
        }
        CustomSpacer(modifier = Modifier.height(12.dp))
        BlackButton(
            name = "예약하기",
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth(),
            onClick = {
                onOrder()
                onDismissRequest(dialogState)
            }
        )
        CustomSpacer(modifier = Modifier.height(16.dp))
        Text(text = "예약 확정 전에는 요금이 청구되지 않습니다.",
            fontSize = 10.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        CustomSpacer(modifier = Modifier.height(30.dp))
        RowTextPrice(title = "₩${DecimalFormat("#,###").format(accommodationResult.fee)} x ${reservation.getStay()}박",
            price = accommodationResult.fee * reservation.getStay().toInt())
        CustomSpacer(modifier = Modifier.height(12.dp))
        RowTextPrice(title = "4% 주 단위 요금 할인", price = 25996, color = Color(0xFF118917))
        CustomSpacer(modifier = Modifier.height(12.dp))
        RowTextPrice(title = "청소비", price = 25996)
        CustomSpacer(modifier = Modifier.height(12.dp))
        RowTextPrice(title = "서비스 수수료", price = 8188)
        CustomSpacer(modifier = Modifier.height(12.dp))
        RowTextPrice(title = "숙박세와 수수료", price = 819)
        CustomSpacer(modifier = Modifier.height(16.dp))
        CustomSpacer(modifier = Modifier.height(1.dp), color = Color.LightGray)
        CustomSpacer(modifier = Modifier.height(16.dp))
        RowTextPrice(title = "합계",
            price = accommodationResult.fee * reservation.getStay()
                .toInt() - 25996 + 25996 + 8188 + 819,
            fontSize = 20.sp)
    }
}

@Composable
private fun RowTextPrice(
    title: String,
    price: Int,
    color: Color = Color.Black,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    val won = if (color == Color(0xFF118917)) "-₩" else "₩"
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(text = title, fontSize = fontSize)
        Text(text = "${won}${DecimalFormat("#,###").format(price)}",
            fontSize = fontSize,
            color = color)
    }
}

@Composable
private fun CustomSpacer(modifier: Modifier, color: Color = Color.Transparent) {
    Spacer(modifier = modifier.fillMaxWidth())
}