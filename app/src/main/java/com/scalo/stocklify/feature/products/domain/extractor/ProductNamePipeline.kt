package com.scalo.stocklify.feature.products.domain.extractor

import com.scalo.stocklify.core.model.models.Product

internal object ProductNamePipeline {

    private const val SCORE_THRESHOLD = 0.25f

    private val QUANTITY_ONLY_LINE = Regex(
        """^\d+(?:\.\d+)?\s*(?:kgs?|g(?:r|m)?|ml|l(?:tr?)?|pcs?|pieces?|packs?|pk)$""",
        RegexOption.IGNORE_CASE
    )

    data class Result(
        val name: String?,
        val price: Double?,
        val quantity: QuantityCandidate?,
        val suggestedProduct: Product?,
        val similarityScore: Float
    )

    fun process(rawBlock: String, existingProducts: List<Product>): Result {

        // Keep original text for quantity extraction
        val normalized = OcrCorrector.correct(rawBlock)

        val quantity = QuantityParser.extract(normalized)
        val price = PriceParser.extract(normalized)?.value

        val lines = normalized
            .lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val nameLines = lines.filterNot { line ->
            QUANTITY_ONLY_LINE.matches(line)
        }

        if (nameLines.isEmpty()) {
            return Result(
                name = null,
                price = price,
                quantity = quantity,
                suggestedProduct = null,
                similarityScore = 0f
            )
        }

        val nameParts = mutableListOf<String>()

        for ((index, line) in nameLines.withIndex()) {
            val score = NameExtractor.scoreLine(
                line = line,
                index = index,
                totalLines = nameLines.size
            )

            if (score >= SCORE_THRESHOLD) {
                nameParts += line
            } else if (nameParts.isNotEmpty()) {
                break
            }
        }

        if (nameParts.isEmpty()) {
            return Result(
                name = null,
                price = price,
                quantity = quantity,
                suggestedProduct = null,
                similarityScore = 0f
            )
        }

        val extractedName = cleanName(
            NameExtractor.toTitleCase(
                nameParts.joinToString(" ")
            ),
            price
        )

        val match = FuzzyMatcher.findBestMatch(
            extractedName,
            existingProducts
        )

        return Result(
            name = extractedName,
            price = price,
            quantity = quantity,
            suggestedProduct = match?.first,
            similarityScore = match?.second ?: 0f
        )
    }

    private fun cleanName(name: String, price: Double?): String {
        var result = name
        if (price != null) {
            val priceStr = if (price == Math.floor(price)) price.toInt().toString() else price.toString()
            result = result.replace(Regex("""[\s\-]+${Regex.escape(priceStr)}$"""), "")
        }
        return result
            .replace(Regex("""\s+"""), " ")
            .trim()
    }
}
