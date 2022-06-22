package com.example.todo.airbnb.presentation.search.searchresult

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.airbnb.domain.model.AccommodationResult
import com.example.todo.airbnb.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _result = mutableStateOf<List<AccommodationResult>>(emptyList())
    val result: State<List<AccommodationResult>> = _result

    init {
        getFilterAccommodation()
    }

    private fun getFilterAccommodation() {
        viewModelScope.launch {
            repository.getFilterAccommodation().collectLatest {
                _result.value = it
            }
        }
    }

    fun onClickFavorite(index: Int) {
        viewModelScope.launch {
            repository.onClickFavorite(index)
        }
    }
}