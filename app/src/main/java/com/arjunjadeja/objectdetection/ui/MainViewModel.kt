package com.arjunjadeja.objectdetection.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjunjadeja.objectdetection.data.MainRepository
import com.arjunjadeja.objectdetection.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _selectedImage = MutableStateFlow<UiState<Bitmap>>(UiState.Loading)
    val selectedImage: StateFlow<UiState<Bitmap>> = _selectedImage

    private val _detectedObjectImage = MutableStateFlow<UiState<Bitmap>>(UiState.Loading)
    val detectedObjectImage: StateFlow<UiState<Bitmap>> = _detectedObjectImage

    fun updateSelectedImage(bitmap: Bitmap) {
        _selectedImage.value = UiState.Success(bitmap)
        getPredictions(bitmap = bitmap)
    }

    private fun getPredictions(bitmap: Bitmap) {
        _detectedObjectImage.value = UiState.Loading
        viewModelScope.launch(Dispatchers.Default) {
            _detectedObjectImage.value = repository.getPredictions(bitmap)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.Default) {
            repository.closePredictionModel()
        }
    }
}