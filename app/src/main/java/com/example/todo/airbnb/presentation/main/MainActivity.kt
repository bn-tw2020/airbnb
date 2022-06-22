package com.example.todo.airbnb.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.airbnb.presentation.main.components.MainScreen
import com.example.todo.airbnb.presentation.reservation.ReservationViewModel
import com.example.todo.airbnb.presentation.search.SearchViewModel
import com.example.todo.airbnb.presentation.search.searchresult.ResultViewModel
import com.example.todo.airbnb.ui.theme.AirbnbTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<SearchViewModel>()
            val reservationViewModel = hiltViewModel<ReservationViewModel>()
            val searchResultViewModel = hiltViewModel<ResultViewModel>()
            AirbnbTheme(viewModel, reservationViewModel, searchResultViewModel)
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun AirbnbTheme(
    viewModel: SearchViewModel,
    reservationViewModel: ReservationViewModel,
    searchResultViewModel: ResultViewModel
) {
    AirbnbTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MainScreen(
                viewModel = viewModel,
                reservationViewModel = reservationViewModel,
                searchResultViewModel = searchResultViewModel
            )
        }
    }
}