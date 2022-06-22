package com.example.todo.airbnb.presentation.search.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.airbnb.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    private val _detailUiState = mutableStateOf<DetailUiState>(DetailUiState.Loading)
    val detailUiState: State<DetailUiState> get() = _detailUiState

    fun getDetailAccommodation(id: Int) = viewModelScope.launch {
        repository.getDetailAccommodation(id).collectLatest {
            _detailUiState.value = DetailUiState.DetailAccommodation(it)
        }
    }
}