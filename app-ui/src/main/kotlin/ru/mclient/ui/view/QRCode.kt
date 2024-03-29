package ru.mclient.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private val map = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")

@Composable
fun QRCode(
    text: String,
    errorCorrection: ErrorCorrectionLevel = ErrorCorrectionLevel.M,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
) {
    val qrCode by produceState<QRCode?>(initialValue = null, key1 = text) {
        value = withContext(Dispatchers.Default) {
            Encoder.encode(text, errorCorrection, map)
        }
    }
    val qr = qrCode
    if (qr != null)
        Canvas(modifier = modifier.defaultMinSize(100.dp)) {
            val size = minOf(size.width, size.height)
            renderQRImage(
                contentColor = contentColor,
                code = qr,
                width = size,
                height = size,
                quietZone = 4f,
            )
        }
    else
        Box(modifier = modifier)
}

private fun DrawScope.renderQRImage(
    contentColor: Color,
    code: QRCode,
    width: Float,
    height: Float,
    quietZone: Float,
) {
    val input = code.matrix ?: throw IllegalStateException()
    val inputWidth = input.width
    val inputHeight = input.height
    val qrWidth = inputWidth + quietZone * 2
    val qrHeight = inputHeight + quietZone * 2
    val outputWidth = width.coerceAtLeast(qrWidth)
    val outputHeight = height.coerceAtLeast(qrHeight)
    val multiple = (outputWidth / qrWidth).coerceAtMost(outputHeight / qrHeight)
    val leftPadding = ((outputWidth - inputWidth * multiple) / 2)
    val topPadding = ((outputHeight - inputHeight * multiple) / 2)
    val FINDER_PATTERN_SIZE = 7
    val CIRCLE_SCALE_DOWN_FACTOR = 21f / 30f
    val circleSize = (multiple * CIRCLE_SCALE_DOWN_FACTOR)
    var inputY = 0
    var outputY = topPadding
    while (inputY < inputHeight) {
        var inputX = 0
        var outputX = leftPadding
        while (inputX < inputWidth) {
            if (input[inputX, inputY].toInt() == 1) {
                if (!(inputX <= FINDER_PATTERN_SIZE && inputY <= FINDER_PATTERN_SIZE || inputX >= inputWidth - FINDER_PATTERN_SIZE && inputY <= FINDER_PATTERN_SIZE || inputX <= FINDER_PATTERN_SIZE && inputY >= inputHeight - FINDER_PATTERN_SIZE)) {
                    drawRoundRect(
                        contentColor,
                        Offset(outputX, outputY),
                        Size(circleSize, circleSize),
                        CornerRadius(2f, 2f),
                    )
                }
            }
            inputX++
            outputX += multiple
        }
        inputY++
        outputY += multiple
    }
    val circleDiameter = multiple * FINDER_PATTERN_SIZE
    drawFinderPatternCircleStyle(
        contentColor,
        leftPadding,
        topPadding,
        circleDiameter
    )
    drawFinderPatternCircleStyle(
        contentColor,
        leftPadding + (inputWidth - FINDER_PATTERN_SIZE) * multiple,
        topPadding,
        circleDiameter
    )
    drawFinderPatternCircleStyle(
        contentColor,
        leftPadding,
        topPadding + (inputHeight - FINDER_PATTERN_SIZE) * multiple,
        circleDiameter
    )
}

private fun DrawScope.drawFinderPatternCircleStyle(
    contentColor: Color,
    x: Float,
    y: Float,
    circleDiameter: Float,
) {
    val middleDiameter = circleDiameter * 3 / 7
    val middleOffset = circleDiameter * 2 / 7
    drawRoundRect(
        contentColor,
        Offset(x + circleDiameter / 11, y + circleDiameter / 11),
        Size(circleDiameter * 5 / 6, circleDiameter * 5 / 6),
        CornerRadius(15f, 15f),
        style = Stroke(12.5f)
    )
    drawRoundRect(
        contentColor,
        Offset(x + middleOffset, y + middleOffset),
        Size(middleDiameter, middleDiameter),
        CornerRadius(7.5f, 7.5f),
    )
}