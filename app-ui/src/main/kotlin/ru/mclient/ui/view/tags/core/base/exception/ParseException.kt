package ru.mclient.ui.view.tags.core.base.exception

/**
 * [ParseException] signals that an error occurred while parsing process.
 */
class ParseException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException("Error while parsing. $message", cause)
