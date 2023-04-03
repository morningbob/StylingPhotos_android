package com.bitpunchlab.android.stylingphotos.retrievePhotos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log

object RetrievePhotosHelper {

    fun getBitmap(uri: Uri, context: Context) : Bitmap? {
        try {
            if (Build.VERSION.SDK_INT < 28) {
                return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                return ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception){
            Log.i("get bitmap", "there is error ${e.localizedMessage}")
            return null
        }
    }
}