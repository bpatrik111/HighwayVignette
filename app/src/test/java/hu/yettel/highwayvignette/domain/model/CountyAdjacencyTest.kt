package hu.yettel.highwayvignette.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CountyAdjacencyTest {

    private val allCountyIds = (11..29).map { "YEAR_$it" }.toSet()

    @Test
    fun `known neighbor pairs are recognized as neighbors`() {
        // Pest (23) és Fejér (16)
        assertTrue(CountyAdjacency.areNeighbors("YEAR_23", "YEAR_16"))
    }

    @Test
    fun `known non-neighbor pairs are not recognized as neighbors`() {
        // Veszprém (28) és Tolna (26)
        assertFalse(CountyAdjacency.areNeighbors("YEAR_28", "YEAR_26"))
        // Zala (29) és Szabolcs-Szatmár-Bereg (25)
        assertFalse(CountyAdjacency.areNeighbors("YEAR_29", "YEAR_25"))
    }

    @Test
    fun `adjacency graph is symmetric`() {
        for (a in allCountyIds) {
            for (b in allCountyIds) {
                if (a == b) continue
                val aToB = CountyAdjacency.areNeighbors(a, b)
                val bToA = CountyAdjacency.areNeighbors(b, a)
                assertTrue(
                    "Asymmetric adjacency: $a->$b is $aToB but $b->$a is $bToA",
                    aToB == bToA
                )
            }
        }
    }

    @Test
    fun `no county is its own neighbor`() {
        for (id in allCountyIds) {
            assertFalse("$id should not be its own neighbor", CountyAdjacency.areNeighbors(id, id))
        }
    }

    @Test
    fun `every county has at least one neighbor`() {
        for (id in allCountyIds) {
            val hasNeighbor = allCountyIds.any { other -> other != id && CountyAdjacency.areNeighbors(id, other) }
            assertTrue("$id has no neighbors at all", hasNeighbor)
        }
    }

    @Test
    fun `isDirectlyConnected is true for empty or single selection`() {
        assertTrue(CountyAdjacency.isDirectlyConnected("YEAR_11", emptySet()))
        assertTrue(CountyAdjacency.isDirectlyConnected("YEAR_11", setOf("YEAR_11")))
    }

    @Test
    fun `isDirectlyConnected is true when candidate borders at least one selected county`() {
        assertTrue(CountyAdjacency.isDirectlyConnected("YEAR_23", setOf("YEAR_16")))
    }

    @Test
    fun `isDirectlyConnected is false when candidate borders none of the selected counties`() {
        assertFalse(CountyAdjacency.isDirectlyConnected("YEAR_29", setOf("YEAR_23", "YEAR_25")))
    }

    @Test
    fun `isConnectedRegion is true for empty and single-element selections`() {
        assertTrue(CountyAdjacency.isConnectedRegion(emptySet()))
        assertTrue(CountyAdjacency.isConnectedRegion(setOf("YEAR_11")))
    }

    @Test
    fun `isConnectedRegion is true for a chain of directly adjacent counties`() {
        assertTrue(CountyAdjacency.isConnectedRegion(setOf("YEAR_23", "YEAR_16", "YEAR_28")))
    }

    @Test
    fun `isConnectedRegion is false when the selection has a disconnected member`() {
        assertFalse(
            CountyAdjacency.isConnectedRegion(setOf("YEAR_23", "YEAR_16", "YEAR_28", "YEAR_25"))
        )
    }
}