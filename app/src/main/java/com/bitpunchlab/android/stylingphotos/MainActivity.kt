package com.bitpunchlab.android.stylingphotos

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bitpunchlab.android.stylingphotos.main.MainScreen
import com.bitpunchlab.android.stylingphotos.main.MainViewModel
import com.bitpunchlab.android.stylingphotos.processPhoto.StyleTransferHelper
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingPhotosTheme
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingScheme

class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {
            StylingPhotosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                        //.padding(top = 50.dp, bottom = 50.dp),
                    color = StylingScheme.greenBackground
                ) {
                    StylingNavigation(application)
                }
            }
        }
    }
}

@Composable
fun StylingNavigation(application: Application) {
    val navController = rememberNavController()

    val mainViewModel = MainViewModel(application)

    NavHost(navController = navController, startDestination = Main.route) {
        composable(Main.route) {
            MainScreen(mainViewModel)
        }
        composable(Result.route) {

        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StylingPhotosTheme {
        Greeting("Android")
    }
}