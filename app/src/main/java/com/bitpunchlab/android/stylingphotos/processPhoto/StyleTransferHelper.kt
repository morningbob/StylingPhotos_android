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
    private val context : Context,
    private var inputPredictTargetHeight: Int = 0,
    private var inputPredictTargetWidth: Int = 0,
    private var outputPredictShape : IntArray = intArrayOf(),
    private var inputTransformTargetHeight : Int = 0,
    private var inputTransformTargetWidth : Int = 0,
    private var outputTransformShape: IntArray = intArrayOf(),
    val styleTransferListener : StyleTransferListener? = null,
    private var interpreterPredict : Interpreter? = null,
    private var interpreterTransform : Interpreter? = null

) {

    init {
        if (setup()) {
            inputPredictTargetHeight = interpreterPredict!!.getInputTensor(0)
                .shape()[1]
            inputPredictTargetWidth = interpreterPredict!!.getInputTensor(0)
                .shape()[2]
            outputPredictShape = interpreterPredict!!.getOutputTensor(0).shape()
            Log.i("style help init", "inputPredict height: $inputPredictTargetHeight, " +
                    "inputPredict width: $inputPredictTargetWidth, outputPredict shape: $outputPredictShape")
            inputTransformTargetHeight =
                interpreterTransform!!.getInputTensor(0)
                    .shape()[1]
            inputTransformTargetWidth = interpreterTransform!!.getInputTensor(0)
                .shape()[2]
            outputTransformShape =
                interpreterTransform!!.getOutputTensor(0).shape()
            Log.i("style help init", "inputTransform height: $inputTransformTargetHeight, " +
                    "inputTransform width: $inputTransformTargetWidth, outputTransform shape: $outputTransformShape")

        } else {
            styleTransferListener?.onError("TFLite failed to init.")
        }
    }

    fun setup() : Boolean {
        val tfLiteOptions = Interpreter.Options()
        tfLiteOptions.numThreads = 2

        try {

            interpreterPredict = Interpreter(
                FileUtil.loadMappedFile(
                    context,
                    "fp16_prediction_1.tflite"
                    //"int8_prediction_1.tflite"
                ),
                tfLiteOptions
            )
            interpreterTransform = Interpreter(
                FileUtil.loadMappedFile(
                    context,
                    "fp16_transfer_1.tflite"
                    //"int8_transfer_1.tflite"

                ),
                tfLiteOptions
            )
            return interpreterPredict != null && interpreterTransform != null
        } catch (e: java.lang.Exception) {
            Log.i("tflite setup", "failed ${e.localizedMessage}")
            styleTransferListener?.onError(e.localizedMessage)
            return false
        }
    }

    fun preprocessImage(bitmap: Bitmap, targetHeight: Int, targetWidth: Int) : TensorImage {

        val processedBitmap = preprocessBitmap(bitmap)

        val cropSize = min(processedBitmap.height, processedBitmap.width)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(targetHeight, targetWidth, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(processedBitmap)

        return imageProcessor.process(tensorImage)
    }

    fun preprocessBitmap(bitmap: Bitmap) : Bitmap {
        //return Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun testPreprocessImageAndLoadTensorImage(bitmap: Bitmap) : TensorImage {
        val processedBitmap = preprocessBitmap(bitmap)

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(processedBitmap)
        return tensorImage

    }

    fun testPreprocessImage(targetImage: Bitmap, stylingImage: Bitmap) : Pair<TensorImage, TensorImage> {
        //if (targetImageBitmap.value != null && stylingImageBitmap.value != null) {
        val testPredictBitmap = preprocessImage(stylingImage, inputPredictTargetHeight, inputPredictTargetWidth)
        val testTransformBitmap = preprocessImage(targetImage, inputTransformTargetHeight, inputTransformTargetWidth)
        return Pair(testPredictBitmap, testTransformBitmap)
        //}
    }

    fun preprocessAndTransform(targetBitmap: Bitmap, stylingBitmap: Bitmap) : Bitmap? {
        val styleImage = preprocessImage(stylingBitmap, inputPredictTargetHeight, inputPredictTargetWidth)
        val predictBuffer = predict(styleImage)
        val targetImage = preprocessImage(targetBitmap, inputTransformTargetHeight, inputTransformTargetWidth)
        if (predictBuffer != null) {
            return transform(targetImage, predictBuffer)
        } else {
            Log.i("transform image", "can't get prediction from styling photo")
            return null
        }
    }
    fun predict(styleTensor: TensorImage) : TensorBuffer? {
        val predictOutput = TensorBuffer.createFixedSize(outputPredictShape, DataType.FLOAT32)

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
            outputTransformShape, DataType.FLOAT32
        )
        Log.i("transform", "target tensor: $targetTensor")
        val transformInput =
            arrayOf(targetTensor.buffer, styleBuffer.buffer)
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
/*
private fun loadModelFile(filename: String): MappedByteBuffer? {
    val fileDescriptor = context.assets.openFd(filename)
    val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = fileInputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declareLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declareLength)
}

 */