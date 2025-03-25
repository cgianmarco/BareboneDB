package pagemanager

import SlottedPage

typealias FileId = String
abstract class PageManager {
    abstract fun write(fileId: FileId, page: SlottedPage)
    abstract fun read(fileId: FileId, pageId: PageId): SlottedPage
}