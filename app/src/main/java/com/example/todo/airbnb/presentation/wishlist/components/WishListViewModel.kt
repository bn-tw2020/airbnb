package com.example.todo.airbnb.presentation.wishlist.components

import androidx.lifecycle.ViewModel
import com.example.todo.airbnb.domain.model.AccommodationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor() : ViewModel() {

    private val _wishList = MutableStateFlow<MutableList<AccommodationResult>>(mutableListOf())
    val wishList: StateFlow<List<AccommodationResult>> = _wishList.asStateFlow()

    fun init(value: List<AccommodationResult>) {
        val filterList = value.filter {
            it.isFavorite.value
        }
        _wishList.value = filterList.toMutableList()
    }
}