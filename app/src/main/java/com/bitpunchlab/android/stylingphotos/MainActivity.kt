package com.bitpunchlab.android.stylingphotos

import android.os.Bundle
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
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingPhotosTheme
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingScheme

class MainActivity : ComponentActivity() {
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
                    StylingNavigation()
                }
            }
        }
    }
}

@Composable
fun StylingNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Main.route) {
        composable(Main.route) {
            MainScreen()
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