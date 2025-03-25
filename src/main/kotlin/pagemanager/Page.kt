package pagemanager

import java.util.concurrent.locks.ReentrantReadWriteLock

typealias PageId = Int
typealias LogSequenceNumber = Int
abstract class Page(
    val id: PageId
) {
    var lsn: LogSequenceNumber? = null
    val lock = ReentrantReadWriteLock()
    var isDirty = false

    abstract fun serialize(): ByteArray
}