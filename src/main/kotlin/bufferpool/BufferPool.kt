package bufferpool

import SlottedPage
import pagemanager.FileId
import pagemanager.PageId
import pagemanager.PageManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

const val MAX_BUFFER_SIZE = 10

class BufferPool(
    private val pageManager: PageManager
) {

    private val buffer = ConcurrentHashMap<Pair<FileId, PageId>, SlottedPage>()
    private val evictionPolicy: EvictionPolicy = FirstPageEvictionPolicy()
    private val flushLock = ReentrantLock()

    fun pageIterator(fileId: FileId, create: Boolean = false) : Sequence<SlottedPage> = sequence {
        var pageId = 0
        while(true) {
            try {
                yield(fetchPage(fileId, pageId, create))
                pageId++
            } catch (e: Exception) {
                break
            }
        }
    }

    fun fetchPage(fileId: FileId, pageId: PageId, create: Boolean): SlottedPage {

        val pageKey = Pair(fileId, pageId)

        return buffer.getOrPut(pageKey) {
            try {
                // Evict a page if buffer is full
                if (buffer.size >= MAX_BUFFER_SIZE) {
                    val (evictedPageId, evictedPage) = evictionPolicy.evict(buffer)

                    if (evictedPage.isDirty) {
                        pageManager.write(fileId, evictedPage)
                    }
                }

                // If not in buffer, read from the file system
                val page = pageManager.read(fileId, pageId).also {
                    buffer[pageKey] = it
                }

                page

            } catch (e: Exception) {

                if (!create) throw e

                // If creation is allowed, allocate a new page
                SlottedPage(pageId)
            }
        }
    }

    fun flush() {
        flushLock.withLock {
            buffer.forEach { (pageKey, page) ->
                val (fileId, _) = pageKey
                pageManager.write(fileId, page)
            }
        }
    }


}