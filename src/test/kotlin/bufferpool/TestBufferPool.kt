package bufferpool

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pagemanager.InMemoryPageManager
import kotlin.test.assertEquals

class TestBufferPool {

    private lateinit var bufferPool: BufferPool

    @BeforeEach
    fun setUp() {
        val pageManager = InMemoryPageManager()
        bufferPool = BufferPool(pageManager)
    }

    @Test
    fun testWriteAndFetchPage() {
        val fileId = "file"
        val pageId = 0

        val page = bufferPool.fetchPage(fileId, pageId, true)
        page.insert(byteArrayOf(1, 2, 3))

        bufferPool.flush()

        val newPage = bufferPool.fetchPage(fileId, pageId, false)
        assertEquals(1, newPage.occupiedSlots)
    }
}