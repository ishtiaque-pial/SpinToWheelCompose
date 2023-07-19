package com.example.spintowheelcompose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spintowheelcompose.R
import com.example.spintowheelcompose.state.SpinToWheelState
import com.example.spintowheelcompose.utils.drawImageInCanvas
import com.example.spintowheelcompose.utils.getBitmapFromVectorDrawable
import com.example.spintowheelcompose.utils.rotateBitmap

@Composable
fun LuckyWheel(
    modifier: Modifier = Modifier,
    state: SpinToWheelState
) {

    val context = LocalContext.current


    val bitmapList = remember(state.items) {
        state.items.map { getBitmapFromVectorDrawable(context, it.icon) }
    }




    Box(modifier = modifier) {

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            rotate(state.rotationAngleState.value) {

                state.items.forEachIndexed { index, item ->
                    val arcAngle = state.tmpAngle.value + (index * state.sweepAngle.value)
                    drawArc(
                        color = item.color,
                        startAngle = arcAngle,
                        sweepAngle = state.sweepAngle.value,
                        useCenter = true,
                    )

                    if (state.startAngle.value + state.sweepAngle.value > 360) {
                        state.startAngle.value = 0f
                    }
                    state.startAngle.value += state.sweepAngle.value
                    drawIntoCanvas {
                        drawImageInCanvas(
                            canvas = it,
                            tmpAngle = arcAngle,
                            bitmap = bitmapList[index].rotateBitmap(state.startAngle.value + 90 - state.sweepHalfAngle.value),
                            mRadius = size.width.toInt(),
                            mCenter = size.width.toInt() / 2,
                            listSize = state.items.size
                        )
                    }


                }
            }
        }

        ImagePointer(modifier = Modifier.align(Alignment.TopCenter))

    }
}

@Composable
fun ImagePointer(modifier: Modifier) {
    Image(
        painterResource(id = R.drawable.red_arrow_down),
        contentDescription = null,
        modifier = modifier

    )
}