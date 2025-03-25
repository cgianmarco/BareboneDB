package pagemanager

import SlottedPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class TestPageManager {

    private lateinit var pageManager: PageManager

    @BeforeEach
    fun setUp() {
        pageManager = InMemoryPageManager()
    }

    @Test
    fun testSaveAndRetrievePage() {
        val page = SlottedPage(0, 100)
        pageManager.write("test", page)

        val retrievedPage = pageManager.read("test", page.id)
        assertNotNull(retrievedPage)
        assertEquals(page.id, retrievedPage!!.id)
    }
}