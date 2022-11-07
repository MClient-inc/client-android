package ru.mclient.ui.record.profile

import androidx.compose.ui.text.AnnotatedString

//class Formatter(
//    private val startTime: LocalTime,
//    private val endTime: LocalTime,
//    private val comeDate: LocalDate
//) {
//
//    private val currentTime: LocalTime = LocalTime.of(20, 0)
//
//    private val currentDate: LocalDate = LocalDate.of(2022, 11, 5)
//
//    fun format(): String {
//        return if (currentDate <= comeDate) {
//            when (comeDate.dayOfMonth - currentDate.dayOfMonth) {
//                0 -> {
//                    val waitHours = startTime.hour.minus(currentTime.hour)
//                    if (waitHours > 1) {
//                        "Клиент придёт через ${waitHours}ч."
//                    } else {
//                        val waitMinutes = startTime.minute.minus(currentTime.minute)
//                        "Клиент придёт через ${waitMinutes}мин."
//                    }
//                }
//
//                1 ->
//                    "Клиент придёт завтра"
//
//                2 ->
//                    "Клиент придёт послезавтра"
//
//                else -> "Клиент придёт в ${startTime.hour}ч. ${startTime.minute}мин. (${comeDate.dayOfMonth}.${comeDate.month}.${comeDate.year})"
//            }
//
//        } else {
//            "Клиент уже должен был прийти"
//        }
//    }
//
//}


private fun String.filterDigits(): String {
    return filter(Char::isDigit).trim()
}

fun String.formatAsRussianNumber(): String {
    var phone = filterDigits()
    if (phone.firstOrNull() != '7') phone = "7$phone"
    return phone.sliceIgnoreLast(0, 11)
}


private fun String.sliceIgnoreLast(start: Int, end: Int = length): String {
    return if (end > length) {
        substring(start, length)
    } else {
        substring(start, end)
    }
}

fun String.toPhoneFormat(): String {
    val input = filterDigits()
    if (!this.startsWith("7"))
        return this
    return buildString {
        append(this@toPhoneFormat)
        insert(0, "+")
        if (input.length > 1)
            insert(2, " (")
        if (input.length >= 5)
            insert(7, ") ")
        if (input.length >= 8)
            insert(12, "-")
        if (input.length >= 10)
            insert(15, "-")
    }
}

fun AnnotatedString.toPhoneFormat(): AnnotatedString {
    return AnnotatedString(toString().toPhoneFormat())
}

fun Long.toMoney(): String {
    return "$this ₽"
}