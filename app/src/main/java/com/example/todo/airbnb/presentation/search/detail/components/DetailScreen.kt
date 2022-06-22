package com.example.todo.airbnb.presentation.search.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.todo.airbnb.R
import com.example.todo.airbnb.common.components.HandleImageResult
import com.example.todo.airbnb.common.components.ToastMessage
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.domain.model.Search
import com.example.todo.airbnb.presentation.main.components.Destinations
import com.example.todo.airbnb.presentation.main.components.HomeSections
import com.example.todo.airbnb.presentation.reservation.ReservationViewModel
import com.example.todo.airbnb.presentation.search.SearchViewModel
import com.example.todo.airbnb.presentation.search.detail.DetailUiState
import com.example.todo.airbnb.presentation.search.detail.DetailViewModel
import com.example.todo.airbnb.presentation.search.searchresult.ResultViewModel
import java.text.DecimalFormat

@Composable
fun DetailScreen(
    navController: NavController,
    searchViewModel: SearchViewModel,
    searchResultViewModel: ResultViewModel,
    reservationViewModel: ReservationViewModel,
    id: Int,
) {
    val viewModel: DetailViewModel = hiltViewModel()
    viewModel.getDetailAccommodation(id)
    when (val state = viewModel.detailUiState.value) {
        is DetailUiState.DetailAccommodation -> {
            DetailContent(
                accommodation = state.detailAccommodations,
                reservation = searchViewModel.searchUiState.value,
                onBack = { navController.navigateUp() },
                onClick = {
                    navController.navigate(Destinations.calendar) {
                        popUpTo(Destinations.calendar) { inclusive = true }
                    }
                },
                onOrder = {
                    reservationViewModel.addReservation(searchViewModel.searchUiState.value, state.detailAccommodations)
                    navController.navigate(HomeSections.Reservation.route) {
                        popUpTo(Destinations.calendar) { inclusive = true }
                    }
                },
                onButtonClickFavorite = { searchResultViewModel.onClickFavorite(it - 1) }
            )
        }
        is DetailUiState.Loading -> {
            ToastMessage(context = LocalContext.current, text = "상세 데이터가 없습니다.")
        }
    }
}

@Composable
fun DetailContent(
    accommodation: AccommodationResult,
    reservation: Search,
    onBack: () -> Unit,
    onClick: () -> Unit,
    onOrder: () -> Unit,
    onButtonClickFavorite: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp)
    ) {
        Box {
            LoadImage(
                accommodation.image,
                Modifier
                    .fillMaxWidth()
                    .height(375.dp)
            )
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircleButton(Icons.Default.ArrowBack, "back", onClick = { onBack() })
                Row {
                    CircleButton(Icons.Default.Share, "share", onClick = {})
                    Spacer(modifier = Modifier.width(10.dp))
                    CircleButton(Icons.Default.Favorite, "favorite",
                        onClick = { onButtonClickFavorite(accommodation.id) }
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
            Spacer(modifier = Modifier.height(8.5.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = accommodation.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row {
                Image(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star image",
                    colorFilter = ColorFilter.tint(Color.Red)
                )
                Spacer(modifier = Modifier.width(5.5.dp))
                Text(text = "${accommodation.starRate}")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "후기 ${accommodation.reviewCount}개")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = accommodation.address)

            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "레지던스 전체")
                    Text(text = "호스트: Jong님")
                }
                LoadImage(
                    accommodation.image,
                    Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .height(50.dp)
                        .width(50.dp)
                )
            }

            Text(text = "최대인원 ${accommodation.maxPerson}명 원룸 ${accommodation.onRoom}개 침대 ${accommodation.bed}개 욕실 ${accommodation.bathRoom}개")

            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = accommodation.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
    BottomReservation(
        accommodation,
        reservation,
        text = if (reservation.isDefault()) "정보 입력하기" else "예약하기",
        onClick = { onClick() },
        onOrder = { onOrder() }
    )
}

@Composable
private fun CircleButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        shape = CircleShape
    ) {
        Icon(imageVector = icon, contentDescription = description)
    }
}


@Composable
private fun LoadImage(imageURL: String?, modifier: Modifier) {
    val painter = rememberImagePainter(data = imageURL)
    if (imageURL == null) {
        HandleImageResult(
            painterState = painter.state,
            onEmpty = {
                Image(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = "Error Image",
                    modifier = modifier,
                    contentScale = ContentScale.FillBounds
                )
            }
        )
    } else {
        Image(
            painter = painter,
            contentDescription = "숙소 이미지",
            modifier = modifier,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun BottomReservation(
    accommodation: AccommodationResult,
    reservation: Search,
    text: String,
    onClick: () -> Unit,
    onOrder: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(start = 5.dp)
            ) {
                if (text == "예약하기") {
                    Text(text = "w${DecimalFormat("#,###").format(accommodation.fee)} / 박")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "${reservation.checkIn} - ${reservation.checkOut}")
                } else {
                    Text(text = "요금을 확인하려면\n날짜를 입력해주세요.")
                }
            }

            var dialogState by remember { mutableStateOf(false) }
            BlackButton(
                name = text,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .padding(vertical = 8.dp, horizontal = 5.dp),
                onClick = {
                    if (text == "예약하기") {
                        dialogState = true
                    } else {
                        if (reservation.isDefault()) {
                            onClick()
                        }
                    }
                }
            ) {
                CustomDialog(
                    dialogState = dialogState,
                    onDismissRequest = { dialogState = !it },
                    content = {
                        DialogContent(dialogState, accommodation, reservation,
                            onDismissRequest = { dialogState = !it },
                            onOrder = { onOrder() }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun BlackButton(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showDialog: @Composable (() -> Unit) = {},
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Text(text = name)
    }
    showDialog()
}