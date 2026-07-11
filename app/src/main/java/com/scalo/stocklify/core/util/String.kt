package com.scalo.stocklify.core.util

fun String.snakeToTitleCase(): String =
    split('_')
        .filter(String::isNotBlank)
        .joinToString(" ") { word ->
            word.lowercase()
                .replaceFirstChar { it.titlecase() }
        }