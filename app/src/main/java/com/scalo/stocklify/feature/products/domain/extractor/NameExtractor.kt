package com.scalo.stocklify.feature.products.domain.extractor

internal data class NameCandidate(val name: String, val confidence: Float)

internal object NameExtractor {

    private const val SCORE_THRESHOLD = 0.25f

    private val NEGATIVE_KEYWORDS = setOf(
        "ingredients", "ingredient", "nutrition", "nutritional", "serving", "servings",
        "calories", "calorie", "protein", "total fat", "saturated", "carbohydrate",
        "sodium", "vitamin", "mineral", "allergen", "contains", "allergy",
        "manufactured", "distributed", "produced", "imported", "packed",
        "address", "avenue", "street", "brgy", "barangay", "city", "province", "philippines",
        "warning", "keep out", "keep away", "store in", "store at", "refrigerate",
        "best before", "expiry", "expires", "use by", "mfd", "best by",
        "net weight", "gross weight", "net wt", "tel", "fax", "www.", "email",
        "lot no", "lot #", "batch", "registration"
    )

    private val DATE_PATTERN = Regex("""\b\d{1,2}[/\-]\d{1,2}[/\-]\d{2,4}\b""")
    private val BARCODE_PATTERN = Regex("""^\d{6,}$""")
    private val QUANTITY_PATTERN = Regex(
        """^\d+(?:\.\d+)?\s*(?:kg|g|ml|l|pcs?|pieces?|packs?)\s*${'$'}""",
        RegexOption.IGNORE_CASE
    )
    private val PRICE_PATTERN = Regex(
        """(?:[₱P]|PHP|SRP|Price)\s*[\d,.]""",
        RegexOption.IGNORE_CASE
    )

    // Common hyphenated compound terms in FMCG products
    private val HYPHENATIONS = listOf(
        Regex("Non Dairy", RegexOption.IGNORE_CASE) to "Non-Dairy",
        Regex("Non Fat", RegexOption.IGNORE_CASE) to "Non-Fat",
        Regex("Low Fat", RegexOption.IGNORE_CASE) to "Low-Fat",
        Regex("No Sugar", RegexOption.IGNORE_CASE) to "No-Sugar",
        Regex("Low Sugar", RegexOption.IGNORE_CASE) to "Low-Sugar",
        Regex("Sugar Free", RegexOption.IGNORE_CASE) to "Sugar-Free",
        Regex("Fat Free", RegexOption.IGNORE_CASE) to "Fat-Free",
        Regex("Ready To Drink", RegexOption.IGNORE_CASE) to "Ready-to-Drink",
        Regex("Ready To Cook", RegexOption.IGNORE_CASE) to "Ready-to-Cook",
    )

    fun extract(lines: List<String>): NameCandidate? {
        if (lines.isEmpty()) return null
        val total = lines.size

        val scored: List<Pair<String, Float>> = lines.mapIndexed { i, line ->
            line to scoreLine(line.trim(), i, total)
        }

        // Collect the first contiguous run of lines that all score above the threshold
        val nameParts = mutableListOf<String>()
        var cumulativeScore = 0f
        for ((line, score) in scored) {
            if (score >= SCORE_THRESHOLD) {
                nameParts += line.trim()
                cumulativeScore += score
            } else if (nameParts.isNotEmpty()) {
                break
            }
        }

        if (nameParts.isEmpty()) return null

        val rawName = nameParts.joinToString(" ")
        val name = finalize(rawName)
        val avgScore = (cumulativeScore / nameParts.size).coerceIn(0f, 1f)

        return NameCandidate(name, avgScore)
    }

    // ---------------------------------------------------------------------------
    // Scoring
    // ---------------------------------------------------------------------------

    internal fun scoreLine(line: String, index: Int, totalLines: Int): Float {
        if (line.isEmpty()) return 0f
        if (!line.any { it.isLetter() }) return 0f

        var score = 0f

        // Positive signals
        score += 0.30f                                              // has letters (already checked)
        if (line.length in 5..50) score += 0.20f
        score += (1f - index.toFloat() / totalLines.coerceAtLeast(1)) * 0.20f  // position bonus
        if (isUpperOrTitleCase(line)) score += 0.15f

        // Negative signals
        val lower = line.lowercase()
        if (NEGATIVE_KEYWORDS.any { lower.contains(it) }) score -= 0.80f
        if (DATE_PATTERN.containsMatchIn(line)) score -= 0.60f
        if (BARCODE_PATTERN.matches(line)) score -= 1.00f
        if (QUANTITY_PATTERN.matches(line)) score -= 0.60f
        if (PRICE_PATTERN.containsMatchIn(line)) score -= 0.60f
        val digitRatio = line.count { it.isDigit() }.toFloat() / line.length
        if (digitRatio > 0.40f) score -= 0.40f

        return score
    }

    private fun isUpperOrTitleCase(line: String): Boolean {
        val letters = line.filter { it.isLetter() }
        if (letters.isEmpty()) return false
        if (letters.all { it.isUpperCase() }) return true
        return line.split(Regex("\\s+")).all { word ->
            word.isEmpty() || !word[0].isLetter() || word[0].isUpperCase()
        }
    }

    // ---------------------------------------------------------------------------
    // Post-processing
    // ---------------------------------------------------------------------------

    private fun finalize(raw: String): String {
        var result = toTitleCase(raw)
        for ((pattern, replacement) in HYPHENATIONS) {
            result = result.replace(pattern, replacement)
        }
        return result.trim()
    }

    internal fun toTitleCase(text: String): String =
        text.split(" ").joinToString(" ") { word ->
            if (word.isEmpty()) word
            else word[0].uppercaseChar() + word.substring(1).lowercase()
        }
}
