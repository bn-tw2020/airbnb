package com.example.todo.airbnb.presentation.reservation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.domain.model.Reservation
import com.example.todo.airbnb.domain.model.Search
import com.example.todo.airbnb.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _reservation = MutableStateFlow<MutableList<Reservation>>(mutableListOf())
    val reservation: StateFlow<MutableList<Reservation>> = _reservation.asStateFlow()

    private val _itemIndex = mutableStateOf<Int?>(null)
    val itemIndex: State<Int?> = _itemIndex

    fun getReservationList() {
        viewModelScope.launch {
            val reservation = repository.getReservationList()
            reservation.collect {
                _reservation.value = it
            }
        }
    }

    fun addReservation(reservation: Search, accommodationResult: AccommodationResult) {
        viewModelScope.launch {
            repository.addReservation(reservation, accommodationResult)
        }
    }

    fun updateItemIndex(index: Int) {
        _itemIndex.value = index
    }

    fun clearReservationList(index: Int) {
        _reservation.value.removeAt(index)
    }

    fun clearItemIndex() {
        _itemIndex.value = null
    }
}