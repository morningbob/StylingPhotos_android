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

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri : StateFlow<Uri?> = _imageUri.asStateFlow()

    val _targetImageBitmap = MutableStateFlow<Bitmap?>(null)
    val targetImageBitmap : StateFlow<Bitmap?> = _targetImageBitmap.asStateFlow()

    val _stylingImageBitmap = MutableStateFlow<Bitmap?>(null)
    val stylingImageBitmap : StateFlow<Bitmap?> = _stylingImageBitmap.asStateFlow()

    val stylingHelper = StyleTransferHelper(application.applicationContext)

    val _resultBitmap = MutableStateFlow<Bitmap?>(null)
    val resultBitmap : StateFlow<Bitmap?> = _resultBitmap.asStateFlow()

    fun updateImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun updateTargetImageBitmap(bitmap: Bitmap) {
        _targetImageBitmap.value = bitmap
    }

    fun updateStylingImageBitmap(bitmap: Bitmap) {
        _stylingImageBitmap.value = bitmap
    }

    fun transformImage() {
        if (stylingImageBitmap.value != null && targetImageBitmap.value != null) {
            val styleImage = stylingHelper.preprocessImage(stylingImageBitmap.value!!)
            val predictBuffer = stylingHelper.predict(styleImage)
            val targetImage = stylingHelper.preprocessImage(targetImageBitmap.value!!)
            if (predictBuffer != null) {
                _resultBitmap.value = stylingHelper.transform(targetImage, predictBuffer)
            } else {
                Log.i("transform image", "can't get prediction from styling photo")
            }
        }
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