package pagemanager

import SlottedPage
import java.util.concurrent.ConcurrentHashMap

class InMemoryPageManager : PageManager() {

    private val pages = ConcurrentHashMap<Pair<FileId, PageId>, ByteArray>()
    override fun write(fileId: FileId, page: SlottedPage) {
        val key = fileId to page.id
        pages[key] = page.serialize()
    }

    override fun read(fileId: FileId, pageId: PageId): SlottedPage {
        val key = fileId to pageId
        val bytes = pages[key] ?: throw Exception("Page not found")
        return SlottedPage.deserialize(bytes, pageId)
    }

}