package ru.mclient.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import ru.mclient.ui.record.profile.toPhoneFormat


object PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(text.toPhoneFormat(), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (text.isBlank()) return 0
                return when {
                    offset <= 1 -> {
                        2
                    }

                    offset <= 4 -> {
                        offset + 3
                    }

                    offset <= 7 -> {
                        offset + 5
                    }

                    offset <= 9 -> {
                        offset + 6
                    }

                    else -> {
                        offset + 7
                    }
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (text.isBlank()) return 0
                return when {
                    offset <= 2 -> {
                        1
                    }

                    offset <= 7 -> {
                        offset - 3
                    }

                    offset <= 12 -> {
                        offset - 5
                    }

                    offset <= 15 -> {
                        offset - 6
                    }

                    else -> {
                        offset - 7
                    }
                }
            }
        })
    }
}
