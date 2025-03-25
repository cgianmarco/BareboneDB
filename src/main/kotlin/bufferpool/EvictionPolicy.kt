package bufferpool

import SlottedPage
import pagemanager.FileId
import pagemanager.PageId

interface EvictionPolicy {
    fun evict(buffer: MutableMap<Pair<FileId, PageId>, SlottedPage>): Pair<Pair<FileId, PageId>, SlottedPage>
}

class FirstPageEvictionPolicy : EvictionPolicy {
    override fun evict(buffer: MutableMap<Pair<FileId, PageId>, SlottedPage>): Pair<Pair<FileId, PageId>, SlottedPage> {
        return buffer.entries.first().toPair()
    }
}

//class LruEvictionPolicy : EvictionPolicy {
//    override fun evict(buffer: MutableMap<Pair<FileId, PageId>, SlottedPage>): Pair<Pair<FileId, PageId>, SlottedPage> {
//        val (evictedPageId, evictedPage) = buffer.minByOrNull { it.value.lastAccessTime }!!
//        return evictedPageId to evictedPage
//    }
//}