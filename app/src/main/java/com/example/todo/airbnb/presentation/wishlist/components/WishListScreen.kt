package com.example.todo.airbnb.presentation.wishlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.todo.airbnb.R
import com.example.todo.airbnb.common.components.HandleImageResult
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.presentation.search.searchresult.ResultViewModel
import java.text.DecimalFormat

@Composable
fun WishListScreen(
    searchResultViewModel: ResultViewModel,
    navController: NavController,
    wishListViewModel: WishListViewModel = hiltViewModel(),
) {
    wishListViewModel.init(searchResultViewModel.result.value)
    val accommodations = wishListViewModel.wishList.value
    Scaffold(
        topBar = {
            WishListTopAppBar(navController)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(start = 16.5.dp, end = 16.5.dp, top = 12.dp, bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            itemsIndexed(accommodations) { _, it ->
                WishListItem(it)
            }
        }
    }
}

@Composable
private fun WishListTopAppBar(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xffF5F5F7)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .height(24.dp)
                    .padding(start = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = Color.Black,
                )
            }
            Text(
                text = "위시리스트",
                fontSize = 20.sp,
                modifier = Modifier
                    .height(24.dp)
                    .padding(start = 41.dp),
                color = Color(0xff010101),
                fontWeight = FontWeight(500)
            )
        }
    }
}

@Composable
private fun WishListItem(
    accommodation: AccommodationResult,
) {
    val decimalFormat = DecimalFormat("#,###")
    Column(
        modifier = Modifier
            .clickable { }
    ) {
        Box {
            LoadImage(imageURL = accommodation.image)
            Box(
                modifier = Modifier
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
                painter =
                if (accommodation.isFavorite.value) {
                    painterResource(id = R.drawable.ic_favorite_selected)
                } else {
                    painterResource(id = R.drawable.ic_favorite)
                },
                contentDescription = "favorite",
                modifier = Modifier
                    .padding(top = 15.dp, end = 8.36.dp)
                    .align(Alignment.TopEnd)
                    .clickable { accommodation.isFavorite.value = !accommodation.isFavorite.value }
            )
        }
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