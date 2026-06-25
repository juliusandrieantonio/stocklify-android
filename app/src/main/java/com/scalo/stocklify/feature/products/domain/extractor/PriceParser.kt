package com.scalo.stocklify.feature.products.domain.extractor

internal data class PriceCandidate(val value: Double, val confidence: Float)

internal object PriceParser {

    private const val MIN_PRICE = 1.0
    private const val MAX_PRICE = 100_000.0

    // Labeled prices: SRP, Price, Retail Price — highest confidence
    private val LABELED = Regex(
        """(?:SRP|Suggested\s+Retail\s+Price|Retail\s+Price|Price)\s*:?\s*[₱P]?\s*(\d[\d,]*(?:\.\d{1,2})?)""",
        RegexOption.IGNORE_CASE
    )

    // Currency-symbol prefixed
    private val CURRENCY_PREFIXED = Regex(
        """(?<![A-Za-z])(?:[₱]|PHP|(?<![A-Za-z])P(?=[0-9]))\s*(\d[\d,]*(?:\.\d{1,2})?)""",
        RegexOption.IGNORE_CASE
    )

    // Bare number trailing after alphabetic content — e.g. "Ligo Sardines 20" or "coco-pandan-50"
    private val TRAILING_BARE = Regex(
        """(?<=[A-Za-z])[\s\-]+(\d+(?:\.\d{1,2})?)(?:\s*)$"""
    )

    // Noise patterns to strip before searching for prices
    private val NOISE_DATE = Regex("""\b\d{1,2}[/\-]\d{1,2}[/\-]\d{2,4}\b""")
    private val NOISE_PERCENT = Regex("""\d+(?:\.\d+)?\s*%""")
    private val NOISE_NUTRITION = Regex(
        """\d+\s*(?:kcal|cal|kJ|mg|mcg|IU|g\s+of|g\s+per)""",
        RegexOption.IGNORE_CASE
    )
    // Product codes: alphanumeric strings like "ABC-1234" or "12345678"
    private val NOISE_CODE = Regex("""(?:[A-Z]{2,}-\d+|\b\d{7,}\b)""")

    fun extract(text: String): PriceCandidate? {
        val cleaned = stripNoise(text)

        // 1. Prefer labeled prices (SRP / Price label)
        LABELED.find(cleaned)?.let { m ->
            val value = normalizeNumber(m.groupValues[1]) ?: return@let
            if (value in MIN_PRICE..MAX_PRICE) return PriceCandidate(value, 0.95f)
        }

        // 2. Currency-prefixed prices — pick the largest reasonable value
        val candidates = mutableListOf<Double>()
        CURRENCY_PREFIXED.findAll(cleaned).forEach { m ->
            val value = normalizeNumber(m.groupValues[1]) ?: return@forEach
            if (value in MIN_PRICE..MAX_PRICE) candidates += value
        }
        if (candidates.isNotEmpty()) {
            return PriceCandidate(candidates.max(), 0.80f)
        }

        // 3. Bare trailing number after alphabetic content — lowest confidence
        TRAILING_BARE.find(cleaned)?.let { m ->
            val value = normalizeNumber(m.groupValues[1]) ?: return@let
            if (value in MIN_PRICE..MAX_PRICE) return PriceCandidate(value, 0.50f)
        }

        return null
    }

    private fun stripNoise(text: String): String {
        var result = text
        result = result.replace(NOISE_DATE, "")
        result = result.replace(NOISE_PERCENT, "")
        result = result.replace(NOISE_NUTRITION, "")
        result = result.replace(NOISE_CODE, "")
        return result
    }

    internal fun normalizeNumber(raw: String): Double? {
        val trimmed = raw.trim()
        val afterComma = trimmed.substringAfter(',', "")
        return when {
            // "1,500.00" or "1,500" → comma is a thousands separator
            trimmed.contains(',') && (afterComma.length == 3 || trimmed.contains('.')) ->
                trimmed.replace(",", "").toDoubleOrNull()
            // "45,50" → comma is a decimal separator (European)
            trimmed.contains(',') && afterComma.length in 1..2 ->
                trimmed.replace(',', '.').toDoubleOrNull()
            else -> trimmed.toDoubleOrNull()
        }
    }
}
