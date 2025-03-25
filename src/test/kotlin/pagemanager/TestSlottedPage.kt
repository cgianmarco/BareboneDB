package pagemanager

import SlottedPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestSlottedPage {

    lateinit var page: SlottedPage

    @BeforeEach
    fun setUp() {
        page = SlottedPage(0, 100)
    }

    @Test
    fun testInsert() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        assertEquals(1, page.occupiedSlots)
    }

    @Test
    fun testInsertAndRetrieve() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        val slot = page.getSlot(0)
        assertEquals(3, slot?.length)
    }

    @Test
    fun testDelete() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        assertTrue(page.delete(0))
        assertEquals(0, page.occupiedSlots)
    }

    @Test
    fun testDeleteNonExistent() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        assertEquals(false, page.delete(1))
    }

    @Test
    fun testRetrieveDeleted() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        assertTrue(page.delete(0))
        assertEquals(null, page.getSlot(0))
    }

    @Test
    fun testSerialize() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        val serialized = page.serialize()
        assertEquals(100, serialized.size)
    }

    @Test
    fun testDeserialize() {
        assertTrue(page.insert(byteArrayOf(1, 2, 3)))
        val serialized = page.serialize()
        val deserialized = SlottedPage.deserialize(serialized, page.id)
        assertEquals(1, deserialized.occupiedSlots)
    }
}