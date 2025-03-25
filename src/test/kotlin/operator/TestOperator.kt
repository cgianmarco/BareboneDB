package operator

import bufferpool.BufferPool
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pagemanager.InMemoryPageManager
import pagemanager.PageManager

class TestOperator {

    private lateinit var pageManager : PageManager
    private lateinit var bufferPool : BufferPool

    @BeforeEach
    fun setUp() {
        pageManager = InMemoryPageManager()
        bufferPool = BufferPool(pageManager)
    }

    @Test
    fun testInsertOperator() {

    }
}