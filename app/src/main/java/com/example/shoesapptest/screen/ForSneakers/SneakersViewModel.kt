package com.example.shoesapptest.screen.ForSneakers


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SneakersViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _sneakersState = MutableStateFlow<NetworkResponseSneakers<List<SneakersResponse>>>(NetworkResponseSneakers.Loading)
    val sneakersState: StateFlow<NetworkResponseSneakers<List<SneakersResponse>>> = _sneakersState

    private val _favoritesState = MutableStateFlow<NetworkResponseSneakers<List<SneakersResponse>>>(NetworkResponseSneakers.Loading)
    val favoritesState: StateFlow<NetworkResponseSneakers<List<SneakersResponse>>> = _favoritesState



    fun fetchSneakersByCategory(category: String) {
        viewModelScope.launch {
            _sneakersState.value = NetworkResponseSneakers.Loading
            _sneakersState.value = authUseCase.getSneakersByCategory(category)
        }
    }

    fun fetchSneakers() {
        viewModelScope.launch {
            _sneakersState.value = NetworkResponseSneakers.Loading
            try {
                val result = authUseCase.getPopularSneakers()
                when(result) {
                    is NetworkResponseSneakers.Success -> {
                        Log.d("DATA", "Received items: ${result.data}")
                        _sneakersState.value = result
                    }
                    is NetworkResponseSneakers.Error -> {
                        Log.e("ERROR", result.errorMessage)
                    }
                    NetworkResponseSneakers.Loading -> {}
                }
            } catch (e: Exception) {
                Log.e("EXCEPTION", "Error: ${e.stackTraceToString()}")
            }
        }
    }
}

