package com.bitpunchlab.android.stylingphotos.main

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bitpunchlab.android.stylingphotos.processPhoto.StyleTransferHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tensorflow.lite.support.image.TensorImage

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //val _imageUri = MutableStateFlow<Uri?>(null)
    //val imageUri : StateFlow<Uri?> = _imageUri.asStateFlow()

    val _targetImageBitmap = MutableStateFlow<Bitmap?>(null)
    val targetImageBitmap : StateFlow<Bitmap?> = _targetImageBitmap.asStateFlow()

    val _stylingImageBitmap = MutableStateFlow<Bitmap?>(null)
    val stylingImageBitmap : StateFlow<Bitmap?> = _stylingImageBitmap.asStateFlow()

    val stylingHelper = StyleTransferHelper(application.applicationContext)

    val _resultBitmap = MutableStateFlow<Bitmap?>(null)
    val resultBitmap : StateFlow<Bitmap?> = _resultBitmap.asStateFlow()

    val _testPredictBitmap = MutableStateFlow<Bitmap?>(null)
    val testPredictBitmap : StateFlow<Bitmap?> = _testPredictBitmap.asStateFlow()

    val _testTransformBitmap = MutableStateFlow<Bitmap?>(null)
    val testTransformBitmap : StateFlow<Bitmap?> = _testTransformBitmap.asStateFlow()

    val _beforeTestPredict = MutableStateFlow<Bitmap?>(null)
    val beforeTestPredict : StateFlow<Bitmap?> = _beforeTestPredict.asStateFlow()

    val _beforeTestTransform = MutableStateFlow<Bitmap?>(null)
    val beforeTestTransform : StateFlow<Bitmap?> = _beforeTestTransform.asStateFlow()

    //fun updateImageUri(uri: Uri) {
    //    _imageUri.value = uri
    //}

    fun updateTargetImageBitmap(bitmap: Bitmap) {
        _targetImageBitmap.value = bitmap
    }

    fun updateStylingImageBitmap(bitmap: Bitmap) {
        _stylingImageBitmap.value = bitmap
    }

    fun transformImage() {
        if (targetImageBitmap.value != null && stylingImageBitmap.value != null) {
            _resultBitmap.value = stylingHelper.preprocessAndTransform(targetImageBitmap.value!!, stylingImageBitmap.value!!)
        }
    }

    fun testPreprocess() {
        if (targetImageBitmap.value != null && stylingImageBitmap.value != null) {
            _beforeTestPredict.value = stylingImageBitmap.value!!
            _beforeTestTransform.value = targetImageBitmap.value
            val result = stylingHelper.testPreprocessImage(targetImageBitmap.value!!, stylingImageBitmap.value!!)
            _testPredictBitmap.value = result.first.bitmap
            _testTransformBitmap.value = result.second.bitmap
        }
    }

    fun testPreprocessBitmap() {
        _beforeTestPredict.value = stylingHelper.preprocessBitmap(stylingImageBitmap.value!!)
    }

    fun testTensorImage() {
        _beforeTestPredict.value = stylingHelper
            .testPreprocessImageAndLoadTensorImage(stylingImageBitmap.value!!)
            .bitmap
    }
 }

class MainViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}