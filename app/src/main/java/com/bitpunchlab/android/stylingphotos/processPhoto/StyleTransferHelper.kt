package com.bitpunchlab.android.stylingphotos.processPhoto

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.DequantizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.min

class StyleTransferHelper(
    val context : Context,
    val styleTransferListener : StyleTransferListener? = null,
    var interpreterPredict : Interpreter? = null,
    var interpreterTransform : Interpreter? = null
) {

    init {
        setup()
    }

    fun setup() {
        val tfLiteOptions = Interpreter.Options()
        tfLiteOptions.numThreads = 2

        try {
            interpreterPredict = Interpreter(
                FileUtil.loadMappedFile(
                    context,
                    "int8_prediction_1.tflite"
                ),
                tfLiteOptions
            )
            interpreterTransform = Interpreter(
                FileUtil.loadMappedFile(
                    context,
                    "int8_transfer_1.tflite"
                ),
                tfLiteOptions
            )
        } catch (e: java.lang.Exception) {
            Log.i("tflite setup", "failed ${e.localizedMessage}")
            styleTransferListener?.onError(e.localizedMessage)
        }
    }

    fun preprocessImage(bitmap: Bitmap) : TensorImage {
        val processedBitmap = preprocessBitmap(bitmap)

        val cropSize = min(processedBitmap.height, processedBitmap.width)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(200,200,ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(processedBitmap)

        return imageProcessor.process(tensorImage)
    }

    private fun preprocessBitmap(bitmap: Bitmap) : Bitmap {
        return Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    }
    fun predict(styleTensor: TensorImage) : TensorBuffer? {
        val predictOutput = TensorBuffer.createFixedSize(intArrayOf(), DataType.FLOAT32)

        if (interpreterPredict != null) {
            Log.i("predict", "predicting")
            interpreterPredict!!.run(styleTensor.buffer, predictOutput.buffer)
            return predictOutput
        } else {
            Log.i("predict", "null interpreter")
            return null
        }
    }

    fun transform(targetTensor: TensorImage, styleBuffer: TensorBuffer) : Bitmap? {
        val outputImage = TensorBuffer.createFixedSize(
            intArrayOf(), DataType.FLOAT32
        )
        val transformInput =
            arrayOf(targetTensor.buffer, styleBuffer)
        if (interpreterTransform != null) {
            Log.i("transform", "transforming")
            //interpreterTransform!!.run(targetTensor.buffer, styleBuffer)
            interpreterTransform?.runForMultipleInputsOutputs(
                transformInput,
                mapOf(Pair(0, outputImage.buffer))
            )
            return getOutputBitmap(outputImage)
        } else {
            Log.i("transform", "null interpreter")
            return null
        }
    }

    fun getOutputBitmap(resultTensor: TensorBuffer) : Bitmap? {

        val tensorImage = TensorImage(DataType.FLOAT32)

        val imageProcessor = ImageProcessor.Builder()
            .add(DequantizeOp(0f, 255f))
            .build()

        tensorImage.load(resultTensor)

        return imageProcessor.process(tensorImage).bitmap

    }

}

interface StyleTransferListener {
    fun onError(error: String)
    fun onResult()
}