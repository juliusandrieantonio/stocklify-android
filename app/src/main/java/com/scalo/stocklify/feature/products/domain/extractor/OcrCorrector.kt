package com.scalo.stocklify.feature.products.domain.extractor

internal object OcrCorrector {

    // Common packaging words that OCR splits with a spurious space
    private val WORD_SPLIT_FIXES: List<Pair<Regex, String>> = listOf(
        Regex("""COF\s+FEE""", RegexOption.IGNORE_CASE) to "COFFEE",
        Regex("""CREA\s+MER""", RegexOption.IGNORE_CASE) to "CREAMER",
        Regex("""CREAM\s+ER""", RegexOption.IGNORE_CASE) to "CREAMER",
        Regex("""CHOCO\s+LATE""", RegexOption.IGNORE_CASE) to "CHOCOLATE",
        Regex("""VINE\s+GAR""", RegexOption.IGNORE_CASE) to "VINEGAR",
        Regex("""FLAV\s+OR""", RegexOption.IGNORE_CASE) to "FLAVOR",
        Regex("""INGRE\s+DIENTS""", RegexOption.IGNORE_CASE) to "INGREDIENTS",
        Regex("""NUTRI\s+TION""", RegexOption.IGNORE_CASE) to "NUTRITION",
        Regex("""VITA\s+MIN""", RegexOption.IGNORE_CASE) to "VITAMIN",
        Regex("""PACK\s+AGE""", RegexOption.IGNORE_CASE) to "PACKAGE",
        Regex("""SATUR\s+ATED""", RegexOption.IGNORE_CASE) to "SATURATED",
        Regex("""CARBO\s+HYDRATE""", RegexOption.IGNORE_CASE) to "CARBOHYDRATE",
        // Additional Filipino FMCG splits
        Regex("""SARD\s+INES?""", RegexOption.IGNORE_CASE) to "SARDINES",
        Regex("""NOOD\s+LES?""", RegexOption.IGNORE_CASE) to "NOODLES",
        Regex("""INSTANT\s+(?=NOODLE)""", RegexOption.IGNORE_CASE) to "INSTANT ",
        Regex("""COND\s+ENSE[D]?""", RegexOption.IGNORE_CASE) to "CONDENSED",
        Regex("""EVAP\s+ORATED""", RegexOption.IGNORE_CASE) to "EVAPORATED",
        Regex("""COOK\s+ING""", RegexOption.IGNORE_CASE) to "COOKING",
        Regex("""PUMP\s+KIN""", RegexOption.IGNORE_CASE) to "PUMPKIN",
        Regex("""KETCH\s+UP""", RegexOption.IGNORE_CASE) to "KETCHUP",
        Regex("""MUST\s+ARD""", RegexOption.IGNORE_CASE) to "MUSTARD",
        Regex("""MAY\s+ONNAISE""", RegexOption.IGNORE_CASE) to "MAYONNAISE",
        Regex("""MAYO\s+NNAISE""", RegexOption.IGNORE_CASE) to "MAYONNAISE",
        Regex("""SHAMP\s+OO""", RegexOption.IGNORE_CASE) to "SHAMPOO",
        Regex("""TOOTHP\s+ASTE""", RegexOption.IGNORE_CASE) to "TOOTHPASTE",
        Regex("""DETER\s+GENT""", RegexOption.IGNORE_CASE) to "DETERGENT",
    )

    // OCR character substitutions in unit context only
    private val UNIT_CHARACTER_FIXES: List<Pair<Regex, String>> = listOf(
        // "500 q" → "500g"  (lowercase q misread for g)
        Regex("""(\d+(?:\.\d+)?)\s*[qQ](?=\s|${'$'})""") to "$1g",
        // "330 mI" → "330ml"  (uppercase I misread for l)
        Regex("""(\d+(?:\.\d+)?)\s*m[Il](?=\s|${'$'})""") to "$1ml",
        // "1 KGS" → "1kg"
        Regex("""(\d+(?:\.\d+)?)\s*KGS?(?=\s|${'$'})""") to "$1kg",
    )

    // Simple single-char confusion fixes (no backreferences needed)
    private val CHARACTER_CONFUSION_FIXES: List<Pair<Regex, String>> = listOf(
        // "l" (lowercase L) misread as "1" between letters: "Mi1k" → "Milk"
        Regex("""(?<=[A-Za-z])1(?=[a-z])""") to "l",
        // "I" (uppercase i) misread as "l" after uppercase run: "MlLO" → "MILO"
        Regex("""(?<=[A-Z])l(?=[A-Z])""") to "I",
        // "S" misread as "5" at the start of a word: "5ardines" → "Sardines"
        Regex("""^5(?=[aA][Rr][Dd])""", RegexOption.MULTILINE) to "S",
        // "B" misread as "8": "8iscuit" → "Biscuit"
        Regex("""^8(?=[Ii][Ss][Cc])""", RegexOption.MULTILINE) to "B",
    )

    // Fixes that need backreferences — handled with lambdas
    private val DIGIT_O_CONFUSION = Regex("""(\d)O(\d)""")

    fun correct(text: String): String {
        var result = text

        for ((pattern, replacement) in WORD_SPLIT_FIXES) {
            result = result.replace(pattern, replacement)
        }

        for ((pattern, replacement) in UNIT_CHARACTER_FIXES) {
            result = result.replace(pattern, replacement)
        }

        for ((pattern, replacement) in CHARACTER_CONFUSION_FIXES) {
            result = result.replace(pattern, replacement)
        }

        // "O" misread as "0" inside digit sequences: "5OO ml" → "500 ml"
        result = DIGIT_O_CONFUSION.replace(result) { m ->
            "${m.groupValues[1]}0${m.groupValues[2]}"
        }

        return result
    }
}
