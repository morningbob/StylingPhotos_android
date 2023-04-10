package com.bitpunchlab.android.stylingphotos.base

import android.widget.NumberPicker.OnValueChangeListener
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
@Composable
fun AppSlider(initValue: Float, valueSet: (Float) -> Unit, valueChangeFinishedListener: (Float) -> Unit,
              modifier: Modifier) {
    var sliderValue by remember { mutableStateOf(initValue) }
    Slider(
        value = sliderValue,
        onValueChange = {
            sliderValue = it
            valueSet.invoke(sliderValue)
        },
        valueRange = 0f..100f,
        onValueChangeFinished = { },//valueSet.invoke(sliderValue) },
        interactionSource = remember { MutableInteractionSource() },
        enabled = true,
        steps = 20,
        colors = SliderDefaults.colors(
            thumbColor = StylingScheme.darkGreen,
            activeTrackColor = StylingScheme.darkGreen
        ),
        modifier = Modifier.then(modifier)
    )
}