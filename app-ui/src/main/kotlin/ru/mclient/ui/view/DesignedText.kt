package ru.mclient.ui.view

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit


sealed interface DesignedString {

    @JvmInline
    value class SimpleString(val text: String) : DesignedString

    data class AnnotatedString(
        val text: androidx.compose.ui.text.AnnotatedString,
        val inlineContent: Map<String, InlineTextContent> = mapOf()
    ) : DesignedString

    @JvmInline
    value class Resource(val text: Int) : DesignedString

}


fun String.toDesignedString(): DesignedString.SimpleString {
    return DesignedString.SimpleString(this)
}

fun AnnotatedString.toDesignedString(): DesignedString.AnnotatedString {
    return DesignedString.AnnotatedString(this)
}

fun Int.toDesignedString(): DesignedString.Resource {
    return DesignedString.Resource(this)
}

@Composable
fun DesignedString.toStringComposable(): String {
    return when (this) {
        is DesignedString.AnnotatedString -> text.toString()
        is DesignedString.Resource -> stringResource(id = text)
        is DesignedString.SimpleString -> text
    }
}

@Composable
fun DesignedText(
    text: DesignedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    when (text) {
        is DesignedString.AnnotatedString ->
            Text(
                text = text.text,
                modifier = modifier,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                inlineContent = text.inlineContent,
                onTextLayout = onTextLayout,
                style = style
            )

        is DesignedString.Resource ->
            Text(
                text = stringResource(text.text),
                modifier = modifier,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                onTextLayout = onTextLayout,
                style = style
            )

        is DesignedString.SimpleString ->
            Text(
                text = text.text,
                modifier = modifier,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                onTextLayout = onTextLayout,
                style = style
            )
    }
}
