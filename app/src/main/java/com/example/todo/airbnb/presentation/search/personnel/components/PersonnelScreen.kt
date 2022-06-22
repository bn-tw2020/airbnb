package com.example.todo.airbnb.presentation.search.personnel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todo.airbnb.R
import com.example.todo.airbnb.common.components.ToastMessage
import com.example.todo.airbnb.domain.model.Personnel
import com.example.todo.airbnb.domain.model.Search
import com.example.todo.airbnb.presentation.main.components.Destinations
import com.example.todo.airbnb.presentation.search.SearchViewModel
import com.example.todo.airbnb.presentation.search.components.common.BottomScreen
import com.example.todo.airbnb.presentation.search.personnel.PersonnelViewModel

@Composable
fun PersonnelScreen(
    navController: NavController,
    searchViewModel: SearchViewModel,
) {
    val viewModel: PersonnelViewModel = hiltViewModel()
    val uiState by viewModel.personnelUiState

    if (uiState.showAlertMessage) {
        ToastMessage(
            context = LocalContext.current,
            text = "유아 및 어린이는 성인과 함께 동반하셔야 됩니다."
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PersonnelTopAppBar(
                    personnel = uiState.personnel,
                    reservation = searchViewModel.searchUiState.value,
                    onAddReservation = { searchViewModel.addReservation(it) },
                    onBackButtonClicked = { navController.navigateUp() },
                    onCheckButtonClicked = { navController.navigate(Destinations.searchResult) }
                )
                TotalPersonnelText(personnel = uiState.personnel)
            }
        },
        bottomBar = {
            BottomScreen(
                title = bottomBarText(uiState.personnel),
                onRemove = { viewModel.clearPersonnel() },
                onSkip = {
                    val reservation = searchViewModel.searchUiState.value
                    searchViewModel.addReservation(reservation.copy(guest = Personnel.defaultOf()))
                    navController.navigate(Destinations.searchResult)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            PersonnelItem(
                PersonnelText = uiState.personnel.adult,
                countPlus = { viewModel.increaseAdultPersonnel() },
                countMinus = { viewModel.decreaseAdultPersonnel() },
                lifeCycle = "성인",
                ageLimit = "만 13세 이상"
            )
            CustomSpacer()
            PersonnelItem(
                PersonnelText = uiState.personnel.child,
                countPlus = { viewModel.increaseChildPersonnel() },
                countMinus = { viewModel.decreaseChildPersonnel() },
                lifeCycle = "어린이",
                ageLimit = "만 2~12세"
            )
            CustomSpacer()
            PersonnelItem(
                PersonnelText = uiState.personnel.baby,
                countPlus = { viewModel.increaseBabyPersonnel() },
                countMinus = { viewModel.decreaseBabyPersonnel() },
                lifeCycle = "유아",
                ageLimit = "만 2세 미만"
            )
        }
    }
}

@Composable
private fun PersonnelTopAppBar(
    personnel: Personnel,
    reservation: Search,
    onAddReservation: (Search) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCheckButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(0xffF5F5F7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onBackButtonClicked() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = Color.Black,
                )
            }
            IconButton(
                onClick = {
                    onAddReservation(reservation.copy(guest = personnel))
                    onCheckButtonClicked()
                },
                enabled = checkButtonEnabled(personnel)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check Icon",
                    tint = checkButtonColor(personnel)
                )
            }
        }
    }
}

private fun checkButtonEnabled(personnel: Personnel): Boolean {
    if (personnel.adult == 0 && personnel.child == 0 && personnel.baby == 0)
        return false
    return true
}

private fun checkButtonColor(personnel: Personnel): Color {
    if (personnel.adult == 0 && personnel.child == 0 && personnel.baby == 0)
        return Color(0xffBDBDBD)
    return Color(0xffE84C60)
}

@Composable
private fun TotalPersonnelText(
    personnel: Personnel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xffF5F5F7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.width(207.dp)
        ) {
            Text(
                text = "인원 입력",
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "게스트 ${personnel.adult + personnel.child}, 유아${personnel.baby}",
                fontSize = 19.sp
            )
        }
    }
}

@Composable
private fun PersonnelItem(
    PersonnelText: Int,
    countPlus: () -> Unit,
    countMinus: () -> Unit,
    lifeCycle: String,
    ageLimit: String,
) {
    Row(
        modifier = Modifier
            .height(75.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = lifeCycle,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = ageLimit,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
        }
        Row(
            modifier = Modifier.height(36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = countMinus,
                enabled = PersonnelText > 0
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_remove_circle),
                    contentDescription = "Remove Icon",
                    tint = minusButtonColors(PersonnelText),
                    modifier = Modifier.size(30.dp),
                )
            }
            Text(
                text = "$PersonnelText"
            )
            IconButton(
                onClick = countPlus,
                enabled = PersonnelText < 8
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_circle),
                    contentDescription = "Add Icon",
                    tint = plusButtonColors(PersonnelText),
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

private fun minusButtonColors(PersonnelText: Int): Color {
    if (PersonnelText == 0) return Color(0xffBDBDBD)
    return Color(0xff333333)
}

private fun plusButtonColors(PersonnelText: Int): Color {
    if (PersonnelText == 8) return Color(0xffBDBDBD)
    return Color(0xff333333)
}

@Composable
private fun CustomSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .border(width = 1.dp, color = Color(0xffE0E0E0))
    )
}

private fun bottomBarText(personnel: Personnel): String {
    if (personnel.adult + personnel.child + personnel.baby == 0) return ""
    return "0이아님"
}