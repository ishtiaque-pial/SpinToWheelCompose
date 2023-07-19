package com.example.spintowheelcompose.state

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.spintowheelcompose.model.LuckyItem
import androidx.compose.runtime.*
import kotlin.random.Random


data class SpinToWheelState(
    val items: List<LuckyItem>,
    val mRoundOfNumber: Int,
    val targetIndex: Int
) {
    val startAngle = mutableStateOf(0f)

    val tmpAngle = mutableStateOf(0f)

    val sweepAngle = mutableStateOf(360f / items.size)
    val sweepHalfAngle = mutableStateOf((360f / items.size) / 2)
    var rotationAngleState by mutableStateOf(Animatable(0f))
    private var animationState by mutableStateOf(AnimationState.STOPPED)

    suspend fun animate(onFinish: (LuckyItem) -> Unit = {}) {
        when (animationState) {
            AnimationState.STOPPED -> {
                spin(onFinish = onFinish)
            }

            AnimationState.SPINNING -> {
                reset()
            }
        }
    }

    private suspend fun spin(onFinish: (LuckyItem) -> Unit = {}) {
        if (animationState == AnimationState.STOPPED) {
            animationState = AnimationState.SPINNING
            val result =
                (360f * mRoundOfNumber) + 270f - (sweepAngle.value * targetIndex) - 360f / items.size / 2
            rotationAngleState.animateTo(
                result,
                animationSpec = tween(durationMillis = (mRoundOfNumber * 1050 + 900L).toInt())
            )
            onFinish(items[targetIndex])
        }
    }

    private suspend fun reset() {
        if (animationState == AnimationState.SPINNING) {

            rotationAngleState.snapTo(0f)

            animationState = AnimationState.STOPPED
        }
    }
}


enum class AnimationState {
    STOPPED, SPINNING
}

@Composable
fun rememberSpinWheelState(
    items: List<LuckyItem>,
    mRoundOfNumber: Int = 10,
    targetIndex: Int = Random.nextInt(items.size)
): SpinToWheelState {
    return remember {
        SpinToWheelState(items,mRoundOfNumber,targetIndex)
    }
}