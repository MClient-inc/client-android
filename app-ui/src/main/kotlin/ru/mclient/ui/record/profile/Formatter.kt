package ru.mclient.ui.record.profile

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


fun String.toPhoneFormat(): String {
    if (this.length != 11 && !this.startsWith("7"))
        return this
    return buildString {
        append(this@toPhoneFormat)
        insert(0, "+")
        insert(2, "(")
        insert(6, ")")
        insert(10, "-")
        insert(13, "-")
    }
}
