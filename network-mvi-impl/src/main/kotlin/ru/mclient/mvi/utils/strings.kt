package ru.mclient.mvi.utils

fun String.sliceIfNeed(range: IntRange): String {
    if (lastIndex > range.last) {
        return slice(range)
    }
    return this
}