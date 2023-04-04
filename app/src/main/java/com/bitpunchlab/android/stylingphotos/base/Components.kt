package com.bitpunchlab.android.stylingphotos.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.bitpunchlab.android.stylingphotos.ui.theme.StylingScheme

@Composable
fun AppButton(content: String, textSize: TextUnit, onClickListener: () -> Unit,
              shouldEnable: Boolean = true,
              modifier: Modifier) {

    OutlinedButton(
        onClick = { onClickListener.invoke() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        modifier = Modifier
            .border(
                BorderStroke(
                    3.dp,
                    StylingScheme.darkGreen,

                ),
                RoundedCornerShape(10.dp))
            .then(modifier),
        //shape = RoundedCornerShape(10.dp),
        enabled = shouldEnable

    ) {
        Text(
            text = content,
            fontSize = textSize,
            color = StylingScheme.darkGreen
        )
    }
}