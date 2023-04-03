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

    val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap : StateFlow<Bitmap?> = _imageBitmap.asStateFlow()

    fun updateImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun updateImageBitmap(bitmap: Bitmap) {
        _imageBitmap.value = bitmap
    }


}