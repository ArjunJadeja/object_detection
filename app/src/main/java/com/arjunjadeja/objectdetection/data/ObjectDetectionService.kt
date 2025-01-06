package com.arjunjadeja.objectdetection.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.arjunjadeja.objectdetection.ml.SsdMobilenetV11Metadata1
import com.arjunjadeja.objectdetection.ui.base.UiState
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import javax.inject.Singleton

@Singleton
class ObjectDetectionService(private val context: Context) {

    private lateinit var model: SsdMobilenetV11Metadata1

    fun getPredictions(bitmap: Bitmap): UiState<Bitmap> {
        return try {
            model = SsdMobilenetV11Metadata1.newInstance(context)

            val paint = Paint()
            val colors = listOf(
                Color.BLUE, Color.GREEN, Color.CYAN, Color.GRAY, Color.BLACK, Color.DKGRAY
            )

            val labels = FileUtil.loadLabels(context, "labels.txt")

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(260, 260, ResizeOp.ResizeMethod.BILINEAR))
                .build()

            var image = TensorImage.fromBitmap(bitmap)
            image = imageProcessor.process(image)
            val outputs = model.process(image)

            val locations = outputs.locationsAsTensorBuffer.floatArray
            val classes = outputs.classesAsTensorBuffer.floatArray
            val scores = outputs.scoresAsTensorBuffer.floatArray

            val imageBitmapWithPredictions = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(imageBitmapWithPredictions)
            val h = imageBitmapWithPredictions.height
            val w = imageBitmapWithPredictions.width

            paint.textSize = h / 15f
            paint.strokeWidth = h / 85f

            scores.forEachIndexed { index, score ->
                if (score > 0.5) {
                    val x = index * 4
                    if (x + 3 < locations.size && classes[index].toInt() in labels.indices) {
                        paint.color = colors[index % colors.size]
                        paint.style = Paint.Style.STROKE
                        canvas.drawRect(
                            RectF(
                                locations[x + 1] * w,
                                locations[x] * h,
                                locations[x + 3] * w,
                                locations[x + 2] * h
                            ),
                            paint
                        )
                        paint.style = Paint.Style.FILL
                        canvas.drawText(
                            "${labels[classes[index].toInt()]} ${"%.2f".format(score)}",
                            locations[x + 1] * w,
                            locations[x] * h,
                            paint
                        )
                    }
                }
            }
            UiState.Success(imageBitmapWithPredictions)
        } catch (e: Exception) {
            UiState.Error("Prediction failed: ${e.localizedMessage}")
        }
    }

    fun closePredictionModel() {
        if (this::model.isInitialized) model.close()
    }
}
