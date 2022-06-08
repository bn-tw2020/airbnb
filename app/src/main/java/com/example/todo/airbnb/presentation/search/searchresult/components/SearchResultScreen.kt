package com.example.todo.airbnb.presentation.search.searchresult.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.todo.airbnb.R
import com.example.todo.airbnb.common.components.HandleImageResult
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.domain.model.Search
import com.example.todo.airbnb.presentation.main.components.Destinations
import com.example.todo.airbnb.presentation.main.components.HomeSections
import com.example.todo.airbnb.presentation.search.SearchViewModel
import com.example.todo.airbnb.presentation.search.searchresult.ResultViewModel
import java.text.DecimalFormat

@Composable
fun SearchResultScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    resultViewModel: ResultViewModel = ResultViewModel(),
) {

    Scaffold(
        topBar = {
            ResultTopBar(
                onNavigateCondition = { navController.navigate(Destinations.searchCondition) },
                onNavigateSearch = {
                    navController.navigate(HomeSections.Search.route) {
                        popUpTo(HomeSections.Search.route) { inclusive = true }
                    }
                },
                search = viewModel.search.value
            )
        }
    ) {
        ResultContent(
            onNavigateDetail = { navController.navigate(Destinations.detail) },
            onNavigateMap = { navController.navigate(Destinations.searchMap) },
            accommodations = resultViewModel.result.value,
            onClickFavorite = { resultViewModel.onClickFavorite(it) }
        )
    }
}

@Composable
fun ResultTopBar(
    onNavigateCondition: () -> Unit,
    onNavigateSearch: () -> Unit,
    search: Search?,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onNavigateCondition() }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Back Icon",
                tint = Color.Black,
            )
        }

        Text(
            text = "${search?.location ?: ""} ${search?.checkIn ?: ""} ${search?.checkOut ?: ""} 게스트 ${search?.guest ?: ""}"
        )

        IconButton(onClick = { onNavigateSearch() }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Check Icon",
                tint = Color.Black,
            )
        }
    }
}

@Composable
private fun ResultContent(
    onNavigateDetail: () -> Unit,
    onNavigateMap: () -> Unit,
    accommodations: List<AccommodationResult>,
    onClickFavorite: (Int) -> Unit,
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .padding(start = 16.5.dp, end = 16.5.dp, top = 12.dp, bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            item {
                Text(
                    text = "${accommodations.size / 10 * 10}개 이상의 숙소",
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            itemsIndexed(accommodations) { index, it ->
                AccommodationItem(
                    it,
                    onNavigate = { onNavigateDetail() },
                    onClick = { onClickFavorite(index) }
                )
            }
        }
        Button(
            onClick = { onNavigateMap() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "지도"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "지도")
        }
    }
}

@Composable
private fun AccommodationItem(
    accommodation: AccommodationResult,
    onNavigate: () -> Unit,
    onClick: () -> Unit,
) {
    val decimalFormat = DecimalFormat("#,###")
    Column(
        modifier = Modifier
            .clickable { onNavigate() }
    ) {
        Box() {
            LoadImage(imageURL = accommodation.image)
            Box(modifier = Modifier
                .padding(top = 15.dp, start = 8.36.dp)
                .clip(RoundedCornerShape(size = 10.dp))
                .align(Alignment.TopStart)
                .background(Color.White)
            ) {
                Text(
                    text = "슈퍼 호스트",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Image(
                painter = if (accommodation.isFavorite.value) painterResource(id = R.drawable.ic_favorite_selected) else painterResource(
                    id = R.drawable.ic_favorite),
                contentDescription = "favorite",
                modifier = Modifier
                    .padding(top = 15.dp, end = 8.36.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onClick() }
            )
        }

        Spacer(modifier = Modifier.height(8.5.dp))

        Row {
            Image(
                imageVector = Icons.Default.Star,
                contentDescription = "star image",
                colorFilter = ColorFilter.tint(Color.Red)
            )
            Spacer(modifier = Modifier.width(5.5.dp))
            Text(text = accommodation.starRate.toString())
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "후기 ${accommodation.reviewCount}개")
        }
        Spacer(modifier = Modifier.height(8.5.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = accommodation.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "W${decimalFormat.format(accommodation.fee)} / 박")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "총액 W${decimalFormat.format(accommodation.total)}")
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
                        .fillMaxWidth()
                        .height(240.dp)
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
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}