package com.bitpunchlab.android.stylingphotos.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitpunchlab.android.stylingphotos.Main
import com.bitpunchlab.android.stylingphotos.R
import com.bitpunchlab.android.stylingphotos.retrievePhotos.RetrievePhotosHelper
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingScheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = MainViewModel()
) {

    val screenContext = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            mainViewModel.updateImageUri(uri)
            val bitmap = RetrievePhotosHelper.getBitmap(uri, screenContext)
            bitmap?.let {
                Log.i("launcher", "got bitmap")
                mainViewModel.updateImageBitmap(it)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = StylingScheme.greenBackground
    ) {

        Scaffold() {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.mipmap.picture),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 50.dp)
                        //.fillMaxWidth(0.35f)
                        .size(200.dp)
                )

                OutlinedButton(
                    onClick = {
                        pickImageLauncher.launch("image/*")
                    },
                    modifier = Modifier

                ) {
                    Text(
                        text = "Upload Target Photo",
                        fontSize = 20.sp
                    )
                }

            }
        }

    }
}