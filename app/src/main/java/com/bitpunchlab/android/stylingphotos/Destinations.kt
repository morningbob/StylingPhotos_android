package com.bitpunchlab.android.stylingphotos

interface Destinations {
    val route : String
}

object Main : Destinations {
    override val route: String = "Home"
}

object Result : Destinations {
    override val route: String = "Result"
}