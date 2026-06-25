package com.scalo.stocklify.feature.products.domain.extractor

import com.scalo.stocklify.core.model.models.Product

internal object FuzzyMatcher {

    private const val SIMILARITY_THRESHOLD = 0.90f

    fun findBestMatch(query: String, products: List<Product>): Pair<Product, Float>? {
        if (products.isEmpty()) return null
        val normalizedQuery = normalize(query)
        return products
            .map { it to jaroWinkler(normalizedQuery, normalize(it.name)).toFloat() }
            .maxByOrNull { it.second }
            ?.takeIf { it.second >= SIMILARITY_THRESHOLD }
    }

    private fun normalize(text: String): String =
        text.lowercase()
            .replace(Regex("[^a-z0-9 ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

    private fun jaro(s1: String, s2: String): Double {
        if (s1 == s2) return 1.0
        if (s1.isEmpty() || s2.isEmpty()) return 0.0

        val matchDist = maxOf(s1.length, s2.length) / 2 - 1
        val s1Matched = BooleanArray(s1.length)
        val s2Matched = BooleanArray(s2.length)

        var matches = 0
        for (i in s1.indices) {
            val start = maxOf(0, i - matchDist)
            val end = minOf(i + matchDist + 1, s2.length)
            for (j in start until end) {
                if (s2Matched[j] || s1[i] != s2[j]) continue
                s1Matched[i] = true
                s2Matched[j] = true
                matches++
                break
            }
        }

        if (matches == 0) return 0.0

        var transpositions = 0
        var k = 0
        for (i in s1.indices) {
            if (!s1Matched[i]) continue
            while (!s2Matched[k]) k++
            if (s1[i] != s2[k]) transpositions++
            k++
        }

        val m = matches.toDouble()
        return (m / s1.length + m / s2.length + (m - transpositions / 2.0) / m) / 3.0
    }

    private fun jaroWinkler(s1: String, s2: String): Double {
        val jaroSim = jaro(s1, s2)
        var prefix = 0
        for (i in 0 until minOf(4, minOf(s1.length, s2.length))) {
            if (s1[i] == s2[i]) prefix++ else break
        }
        return jaroSim + prefix * 0.1 * (1.0 - jaroSim)
    }
}
