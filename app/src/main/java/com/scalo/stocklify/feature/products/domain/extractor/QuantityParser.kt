package com.scalo.stocklify.feature.products.domain.extractor

internal data class QuantityCandidate(val value: Double, val unit: String, val confidence: Float)

internal object QuantityParser {

    // Canonical form for each accepted unit alias
    private val UNIT_ALIASES = mapOf(
        "kg" to "kg",
        "kgs" to "kg",
        "g" to "g",
        "gr" to "g",
        "gm" to "g",
        "ml" to "ml",
        "l" to "l",
        "lt" to "l",
        "ltr" to "l",
        "pcs" to "pcs",
        "pc" to "pc",
        "piece" to "pc",
        "pieces" to "pcs",
        "pack" to "pack",
        "packs" to "pack",
        "pk" to "pack",
    )

    private val QUANTITY_PATTERN = Regex(
        """(\d+(?:\.\d+)?)\s*-?\s*(kg s?|kgs?|g(?:r|m)?|ml|l(?:tr?)?|pcs?|pieces?|packs?|pk)(?!\w)""",
        setOf(RegexOption.IGNORE_CASE)
    )

    fun extract(text: String): QuantityCandidate? {
        val match = QUANTITY_PATTERN.find(text) ?: return null
        val value = match.groupValues[1].toDoubleOrNull() ?: return null
        val rawUnit = match.groupValues[2].trim().lowercase()
        val unit = UNIT_ALIASES[rawUnit] ?: rawUnit
        return QuantityCandidate(value, unit, 0.90f)
    }
}
