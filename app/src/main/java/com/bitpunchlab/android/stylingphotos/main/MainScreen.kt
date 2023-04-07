package com.bitpunchlab.android.stylingphotos.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitpunchlab.android.stylingphotos.Main
import com.bitpunchlab.android.stylingphotos.R
import com.bitpunchlab.android.stylingphotos.base.AppButton
import com.bitpunchlab.android.stylingphotos.helpers.PhotoType
import com.bitpunchlab.android.stylingphotos.retrievePhotos.RetrievePhotosHelper
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingScheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {

    val targetBitmap by mainViewModel.targetImageBitmap.collectAsState()
    val stylingBitmap by mainViewModel.stylingImageBitmap.collectAsState()
    val resultBitmap by mainViewModel.resultBitmap.collectAsState()
    val testPredict by mainViewModel.testPredictBitmap.collectAsState()
    val testTransform by mainViewModel.testTransformBitmap.collectAsState()
    val beforePredict by mainViewModel.beforeTestPredict.collectAsState()
    val beforeTransform by mainViewModel.beforeTestTransform.collectAsState()

    var photoType = PhotoType.TARGET_PHOTO

    val screenContext = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            //mainViewModel.updateImageUri(uri)
            val bitmap = RetrievePhotosHelper.getBitmap(uri, screenContext)
            bitmap?.let {
                Log.i("launcher", "got bitmap")
                when (photoType) {
                    PhotoType.TARGET_PHOTO -> {
                        mainViewModel.updateTargetImageBitmap(it)
                    }
                    PhotoType.STYLING_PHOTO -> {
                        mainViewModel.updateStylingImageBitmap(it)
                    }
                }

            }
        }
    }

    val uploadTargetOnClicked = {
        photoType = PhotoType.TARGET_PHOTO
        pickImageLauncher.launch("image/*")
    }

    val uploadStylingOnClicked = {
        photoType = PhotoType.STYLING_PHOTO
        pickImageLauncher.launch("image/*")
    }

    val transformClicked = {
        mainViewModel.transformImage()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Scaffold(
            modifier = Modifier
                //.background(StylingScheme.greenBackground),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(StylingScheme.greenBackground)
                    .verticalScroll(rememberScrollState()),

            ) {
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
                            .size(200.dp)
                    )

                    if (beforePredict != null) {
                        Image(
                            bitmap = beforePredict!!.asImageBitmap(),
                            contentDescription = "The output photo.",
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .size(200.dp)
                        )
                    }

                    if (beforeTransform != null) {
                        Image(
                            bitmap = beforeTransform!!.asImageBitmap(),
                            contentDescription = "The output photo.",
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .size(200.dp)
                        )
                    }

                    if (resultBitmap != null) {
                        Image(
                            bitmap = resultBitmap!!.asImageBitmap(),
                            contentDescription = "The output photo.",
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .size(200.dp)
                        )
                    }

                    if (testPredict != null) {
                        Image(
                            bitmap = testPredict!!.asImageBitmap(),
                            contentDescription = "The output photo.",
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .size(200.dp)
                        )
                    }

                    if (testTransform != null) {
                        Image(
                            bitmap = testTransform!!.asImageBitmap(),
                            contentDescription = "The output photo.",
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .size(200.dp)
                        )
                    }

                    AppButton(
                        content = stringResource(id = R.string.upload_target_photo),
                        textSize = 20.sp,
                        onClickListener = uploadTargetOnClicked,
                        modifier = Modifier
                        //.padding(top = 20.dp)
                    )

                    AppButton(
                        content = stringResource(id = R.string.upload_styling_photo),
                        textSize = 20.sp,
                        onClickListener = uploadStylingOnClicked,
                        modifier = Modifier
                        //.padding(top = 20.dp)
                    )

                    if (targetBitmap != null && stylingBitmap != null) {
                        AppButton(
                            content = "Transform",
                            textSize = 20.sp,
                            onClickListener = transformClicked,
                            modifier = Modifier
                        )
                    }

                    if (targetBitmap != null) {
                        val imageBitmap = targetBitmap!!.asImageBitmap()
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "The chosen taget picture",
                            modifier = Modifier
                                //.aspectRatio(ratio = (imageBitmap.height /
                                //       imageBitmap.width).toFloat()
                                //)
                                .size(300.dp)
                                //.fillMaxWidth()
                                .padding(top = 30.dp, bottom = 30.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 20.dp))

                    if (stylingBitmap != null) {
                        //val stylingPainter = painterResource(id = R.mipmap.picture)
                        val imageBitmap = stylingBitmap!!.asImageBitmap()

                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "The chosen styling picture",
                            modifier = Modifier
                                //.aspectRatio(ratio = (imageBitmap.height /
                                //        imageBitmap.width).toFloat()
                                //)
                                .size(300.dp)
                                //.fillMaxWidth(0.8f)
                                .padding(top = 30.dp, bottom = 30.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 20.dp))

                }
            }
        }

    }
}
