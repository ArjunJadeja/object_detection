package com.arjunjadeja.objectdetection.data

import android.graphics.Bitmap
import com.arjunjadeja.objectdetection.ui.base.UiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val objectDetectionService: ObjectDetectionService
) {
    fun getPredictions(bitmap: Bitmap): UiState<Bitmap> {
        return objectDetectionService.getPredictions(bitmap)
    }

    fun closePredictionModel() {
        objectDetectionService.closePredictionModel()
    }
}
