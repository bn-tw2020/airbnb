package com.example.todo.airbnb.presentation.search.date.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todo.airbnb.data.model.date.CalendarDay
import com.example.todo.airbnb.data.model.date.CalendarMonth
import com.example.todo.airbnb.data.model.date.CalendarYear
import com.example.todo.airbnb.data.model.date.DaySelected
import com.example.todo.airbnb.presentation.main.components.Destinations
import com.example.todo.airbnb.presentation.search.SearchViewModel
import com.example.todo.airbnb.presentation.search.components.common.BottomScreen
import com.example.todo.airbnb.presentation.search.date.DateViewModel
import com.example.todo.airbnb.ui.theme.Gray

@Composable
fun DateScreen(navController: NavController, viewModel: SearchViewModel) {
    val dateViewModel: DateViewModel = hiltViewModel()
    val dateState = dateViewModel.dates.value

    CalendarContent(
        selectedDates = dateViewModel.dates.value,
        calendarYear = dateState.year,
        onDayClicked = { calendarDay, calendarMonth ->
            dateViewModel.onDaySelected(
                DaySelected(
                    calendarDay.value.toInt(),
                    calendarMonth,
                    dateState.year
                )
            )
        },
        onClear = { dateViewModel.onClear() },
        dateViewModel = dateViewModel,
        onAddReservation = { checkIn, checkOut -> viewModel.addReservation(checkIn, checkOut) },
        onBackButtonClicked = { navController.navigateUp() },
        onCheckButtonClicked = { navController.navigate(Destinations.fare) },
    )
}

@Composable
fun CalendarContent(
    selectedDates: DatesSelectedState,
    calendarYear: CalendarYear,
    onDayClicked: (CalendarDay, CalendarMonth) -> Unit,
    onClear: () -> Unit,
    dateViewModel: DateViewModel,
    onAddReservation: (String, String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCheckButtonClicked: () -> Unit,
) {
    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            CalendarTopAppBar(
                selectedDates,
                checkIn = dateViewModel.dates.value.from.toString(),
                checkOut = dateViewModel.dates.value.to.toString(),
                onBackButtonClicked = { onBackButtonClicked() },
                onCheckButtonClicked = { onCheckButtonClicked() },
                onAddReservation = { checkIn, checkOut ->
                    onAddReservation(checkIn, checkOut)
                }
            )
        },
        bottomBar = {
            BottomScreen(
                selectedDates.toString(),
                onRemove = { onClear() },
                onSkip = { onCheckButtonClicked() }
            )
        }
    ) {
        Calendar(calendarYear, onDayClicked)
    }
}

@Composable
fun CalendarTopAppBar(
    selectedDates: DatesSelectedState,
    checkIn: String,
    checkOut: String,
    onBackButtonClicked: () -> Unit,
    onCheckButtonClicked: () -> Unit,
    onAddReservation: (String, String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(153.dp),
        elevation = 0.dp,
        color = Gray
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val color = if (selectedDates.toString().isEmpty()) {
                    Color.LightGray
                } else Color.Red
                IconButton(
                    onClick = { onBackButtonClicked() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Icon",
                        tint = Color.Black,
                    )
                }
                IconButton(
                    onClick = {
                        if (selectedDates.toString().isNotEmpty()) {
                            if (checkOut != DatesSelectedState.DEFAULT_DAY) {
                                onAddReservation(checkIn, checkOut)
                            } else {
                                onAddReservation(checkIn, checkIn)
                            }
                            onCheckButtonClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check Icon",
                        tint = color
                    )
                }
            }
            Text(
                text = "여행 일정",
                modifier = Modifier.padding(start = 76.dp)
            )
            if (selectedDates.toString().isEmpty()) {
                Text(
                    text = "날짜를 골라주세요.",
                    modifier = Modifier.padding(start = 76.dp),
                    style = MaterialTheme.typography.h5
                )
            } else {
                Text(
                    text = selectedDates.toString(),
                    modifier = Modifier.padding(start = 76.dp),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}