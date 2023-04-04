package com.bitpunchlab.android.stylingphotos.processPhoto

object StyleTransferHelper {

    val styleTransferListener : StyleTransferListener? = null

    
}

interface StyleTransferListener {
    fun onError(error: String)
    fun onResult()
}