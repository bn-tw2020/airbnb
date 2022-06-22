package com.example.todo.airbnb.presentation.search.searchmap.components

import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.todo.airbnb.R
import com.example.todo.airbnb.common.components.HandleImageResult
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.presentation.search.searchresult.ResultViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.text.DecimalFormat

@ExperimentalPagerApi
@Composable
fun SearchMapScreen(viewModel: ResultViewModel = hiltViewModel()) {
    SearchMapContent(
        onClickFavorite = { viewModel.onClickFavorite(it) },
        accommodation = viewModel.result.value
    )
}

@ExperimentalPagerApi
@Composable
fun SearchMapContent(
    onClickFavorite: (Int) -> Unit,
    accommodation: List<AccommodationResult>,
) {
    val uiSettings = remember { MapUiSettings(zoomControlsEnabled = true) }
    var expanded by remember { mutableStateOf(false) }

    val localContext = LocalContext.current
    val geocoder = Geocoder(localContext)
    val list: MutableList<Address> = mutableListOf()
    accommodation.forEach {
        list.add(geocoder.getFromLocationName(it.name, 1)[0])
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(list[0].latitude, list[0].longitude), 13f)
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
            properties = MapProperties(),
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState,
        ) {
            list.forEachIndexed { index, it ->
                Marker(
                    position = LatLng(it.latitude, it.longitude),
                    title = DecimalFormat("#,###").format(accommodation[index].fee),
                )
            }
        }

        ListButton(
            Modifier
                .padding(start = 16.dp, top = 40.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White),
            onExpanded = { expanded = !expanded },
            expanded = expanded,
            accommodation,
            onClick = { onClickFavorite(it) }
        )
        AccommodationPagerScreen(accommodation, onClick = { onClickFavorite(it) })
    }
}

@ExperimentalPagerApi
@Composable
private fun AccommodationPagerScreen(
    accommodation: List<AccommodationResult>,
    onClick: (Int) -> Unit,
) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, bottom = 24.dp)
    ) {
        HorizontalPager(
            count = accommodation.size,
            state = pagerState,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .height(120.dp)
                .width(300.dp)
                .padding(end = 10.dp)
        ) { page ->
            Item(accommodation[page], onClick = { onClick(page) })
        }
    }
}

@Composable
private fun Item(
    accommodation: AccommodationResult,
    onClick: () -> Unit,
) {
    Row {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(size = 10.dp))
                .background(Color.White)
                .width(112.5.dp)
                .height(120.dp)
        ) {
            LoadImage(accommodation.image)
            Surface(
                modifier = Modifier
                    .padding(top = 10.dp, start = 8.36.dp)
                    .clip(RoundedCornerShape(size = 10.dp))
                    .align(Alignment.TopStart)
                    .background(Color.White)
            ) {
                Text(
                    text = "슈퍼 호스트",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(
                start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp
            )
        ) {
            Row {
                Image(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star image",
                    colorFilter = ColorFilter.tint(Color.Red)
                )
                Spacer(modifier = Modifier.width(5.5.dp))
                Text(text = accommodation.starRate.toString())
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "(${accommodation.reviewCount})")
                Image(
                    painter = if (accommodation.isFavorite.value) painterResource(id = R.drawable.ic_favorite_selected) else painterResource(
                        id = R.drawable.ic_favorite
                    ),
                    contentDescription = "favorite",
                    modifier = Modifier.clickable { onClick() }
                )
                Spacer(modifier = Modifier.width(10.25.dp))
            }
            Spacer(modifier = Modifier.height(6.25.dp))

            Text(
                text = accommodation.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "W${DecimalFormat("#,###").format(accommodation.fee)} / 박")
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun ListButton(
    modifier: Modifier,
    onExpanded: () -> Unit,
    expanded: Boolean,
    accommodation: List<AccommodationResult>,
    onClick: (Int) -> Unit,
) {
    Button(
        onClick = { onExpanded() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White
        )
    ) {
        Image(imageVector = Icons.Default.Menu, contentDescription = "menu")
        Text(text = "목록")
    }

    if (expanded) {
        Surface(
            modifier = Modifier
                .padding(top = 90.dp, start = 16.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(400.dp)
                    .width(350.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                itemsIndexed(accommodation) { index, it ->
                    Item(accommodation[index], onClick = { onClick(index) })
                }
            }
        }
    }
}

@Composable
private fun LoadImage(imageURL: String?) {
    val painter = rememberImagePainter(data = imageURL)
    if (imageURL == null) {
        HandleImageResult(
            painterState = painter.state,
            onEmpty = {
                Image(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = "Error Image",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentScale = ContentScale.FillBounds
                )
            }
        )
    } else {
        Image(
            painter = painter,
            contentDescription = "숙소 이미지",
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}