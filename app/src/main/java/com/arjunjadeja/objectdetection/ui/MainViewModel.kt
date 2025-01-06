package com.arjunjadeja.objectdetection.ui

import androidx.lifecycle.ViewModel
import com.arjunjadeja.objectdetection.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {  }