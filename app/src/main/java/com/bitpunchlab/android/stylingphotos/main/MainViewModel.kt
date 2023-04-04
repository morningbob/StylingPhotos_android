package com.bitpunchlab.android.stylingphotos.main

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri : StateFlow<Uri?> = _imageUri.asStateFlow()

    val _targetImageBitmap = MutableStateFlow<Bitmap?>(null)
    val targetImageBitmap : StateFlow<Bitmap?> = _targetImageBitmap.asStateFlow()

    val _stylingImageBitmap = MutableStateFlow<Bitmap?>(null)
    val stylingImageBitmap : StateFlow<Bitmap?> = _stylingImageBitmap.asStateFlow()

    fun updateImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun updateTargetImageBitmap(bitmap: Bitmap) {
        _targetImageBitmap.value = bitmap
    }

    fun updateStylingImageBitmap(bitmap: Bitmap) {
        _stylingImageBitmap.value = bitmap
    }
}