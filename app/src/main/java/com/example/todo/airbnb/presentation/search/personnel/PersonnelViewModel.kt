package com.example.todo.airbnb.presentation.search.personnel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todo.airbnb.domain.model.Personnel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonnelViewModel @Inject constructor() : ViewModel() {

    private val _personnelUiState = mutableStateOf(PersonnelUiState.defaultOf())
    val personnelUiState: State<PersonnelUiState> get() = _personnelUiState

    fun clearPersonnel() {
        val personnelUiState = _personnelUiState.value
        _personnelUiState.value = personnelUiState.copy(
            personnel = Personnel(0, 0, 0)
        )
    }

    fun increaseAdultPersonnel() {
        val personnelUiState = _personnelUiState.value
        val personnel = personnelUiState.personnel
        _personnelUiState.value = personnelUiState.copy(
            showAlertMessage = false,
            personnel = personnel.copy(adult = personnel.adult + 1)
        )
    }

    fun increaseChildPersonnel() {
        val personnelUiState = _personnelUiState.value
        val personnel = personnelUiState.personnel

        if (personnel.adult == 0) {
            _personnelUiState.value = personnelUiState.copy(
                showAlertMessage = true
            )
        } else {
            _personnelUiState.value = personnelUiState.copy(
                showAlertMessage = false,
                personnel = personnel.copy(child = personnel.child + 1)
            )
        }
    }

    fun increaseBabyPersonnel() {
        val personnelUiState = _personnelUiState.value
        val personnel = personnelUiState.personnel
        if (personnel.adult == 0) {
            _personnelUiState.value = personnelUiState.copy(
                showAlertMessage = true
            )
        } else {
            _personnelUiState.value = personnelUiState.copy(
                showAlertMessage = false,
                personnel = personnel.copy(baby = personnel.baby + 1)
            )
        }
    }

    fun decreaseAdultPersonnel() {
        val personnelUiState = _personnelUiState.value
        val personnel = personnelUiState.personnel
        if ((personnel.child >= 1 || personnel.baby >= 1) && personnel.adult == 1) {
            _personnelUiState.value = personnelUiState.copy(
                showAlertMessage = true
            )
        } else {
            _personnelUiState.value = personnelUiState.copy(
                showAlertMessage = false,
                personnel = personnel.copy(adult = personnel.adult - 1)
            )
        }
    }

    fun decreaseChildPersonnel() {
        val personnelUiState = _personnelUiState.value
        val personnel = personnelUiState.personnel
        _personnelUiState.value = personnelUiState.copy(
            showAlertMessage = false,
            personnel = personnel.copy(child = personnel.child - 1)
        )
    }

    fun decreaseBabyPersonnel() {
        val personnelUiState = _personnelUiState.value
        val personnel = personnelUiState.personnel
        _personnelUiState.value = personnelUiState.copy(
            showAlertMessage = false,
            personnel = personnel.copy(baby = personnel.baby - 1)
        )
    }

    fun alertMessageShown() {
        val personnelUiState = _personnelUiState.value
        _personnelUiState.value = personnelUiState.copy(
            showAlertMessage = false,
        )
    }
}