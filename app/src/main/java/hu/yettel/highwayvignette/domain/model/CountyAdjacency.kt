package hu.yettel.highwayvignette.domain.model

object CountyAdjacency {

    private val adjacency: Map<String, Set<String>> = mapOf(
        "YEAR_11" to setOf("YEAR_23", "YEAR_20", "YEAR_15", "YEAR_26", "YEAR_16", "YEAR_12"), // Bács-Kiskun
        "YEAR_12" to setOf("YEAR_24", "YEAR_26", "YEAR_11"), // Baranya
        "YEAR_13" to setOf("YEAR_15", "YEAR_20", "YEAR_18"), // Békés
        "YEAR_14" to setOf("YEAR_22", "YEAR_19", "YEAR_20", "YEAR_18", "YEAR_25"), // Borsod-Abaúj-Zemplén
        "YEAR_15" to setOf("YEAR_11", "YEAR_20", "YEAR_13"), // Csongrád
        "YEAR_16" to setOf("YEAR_21", "YEAR_28", "YEAR_26", "YEAR_11", "YEAR_23", "YEAR_24"), // Fejér
        "YEAR_17" to setOf("YEAR_27", "YEAR_28", "YEAR_21"), // Győr-Moson-Sopron
        "YEAR_18" to setOf("YEAR_14", "YEAR_20", "YEAR_13", "YEAR_25"), // Hajdú-Bihar
        "YEAR_19" to setOf("YEAR_22", "YEAR_23", "YEAR_20", "YEAR_14"), // Heves
        "YEAR_20" to setOf("YEAR_23", "YEAR_19", "YEAR_14", "YEAR_18", "YEAR_13", "YEAR_15", "YEAR_11"), // Jász-Nagykun-Szolnok
        "YEAR_21" to setOf("YEAR_17", "YEAR_28", "YEAR_16", "YEAR_23"), // Komárom-Esztergom
        "YEAR_22" to setOf("YEAR_23", "YEAR_19", "YEAR_14"), // Nógrád
        "YEAR_23" to setOf("YEAR_21", "YEAR_16", "YEAR_11", "YEAR_20", "YEAR_19", "YEAR_22"), // Pest
        "YEAR_24" to setOf("YEAR_27", "YEAR_28", "YEAR_26", "YEAR_12", "YEAR_29", "YEAR_16"), // Somogy
        "YEAR_25" to setOf("YEAR_14", "YEAR_18"), // Szabolcs-Szatmár-Bereg
        "YEAR_26" to setOf("YEAR_24", "YEAR_12", "YEAR_11", "YEAR_16"), // Tolna
        "YEAR_27" to setOf("YEAR_17", "YEAR_28", "YEAR_24", "YEAR_29"), // Vas
        "YEAR_28" to setOf("YEAR_17", "YEAR_27", "YEAR_24", "YEAR_26", "YEAR_16", "YEAR_21", "YEAR_29"), // Veszprém
        "YEAR_29" to setOf("YEAR_27", "YEAR_28", "YEAR_24") // Zala
    )

    fun areNeighbors(a: String, b: String): Boolean = adjacency[a]?.contains(b) == true

    fun isDirectlyConnected(candidate: String, alreadySelected: Set<String>): Boolean {
        if (alreadySelected.isEmpty() || alreadySelected.contains(candidate)) return true
        return alreadySelected.any { areNeighbors(candidate, it) }
    }
}