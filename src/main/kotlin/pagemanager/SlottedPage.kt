import pagemanager.Page
import pagemanager.PageId
import java.util.concurrent.ConcurrentHashMap
import java.nio.ByteBuffer

const val SLOTTED_PAGE_SIZE = 4096

typealias SlotId = Int

data class Slot(
    var isDeleted: Boolean,
    val length: Short,
    val data: ByteArray
)

class SlottedPage(
    id: PageId,
    private val size: Int = SLOTTED_PAGE_SIZE
) : Page(id) {
    val slots = ConcurrentHashMap<SlotId, Slot>()

    val occupiedSlots: Int
        get() = slots.filter { !it.value.isDeleted }.size

    fun canStore(bytes: ByteArray): Boolean {
        var occupied = 2 + slots.size * (2 + 1 + 2)
        for (slot in slots.values) {
            occupied += slot.data.size
        }
        return occupied + (2 + 1 + 2) + bytes.size < size
    }

    fun insert(bytes: ByteArray): Boolean {
        if (canStore(bytes)) {
            val slotId = slots.size
            val slotLength = bytes.size

            val slot = Slot(
                false,
                slotLength.toShort(),
                bytes
            )

            slots[slotId] = slot
            return true
        }

        return false
    }

    fun delete(slotId: SlotId): Boolean {
        if (!slots.containsKey(slotId)) {
            return false
        }

        slots[slotId]?.isDeleted = true
        return true
    }

    fun getSlot(slotId: SlotId): Slot? {
        val slot = slots[slotId]
        return if (slot?.isDeleted == true) null else slot
    }

    override fun serialize(): ByteArray {
        val buffer = ByteBuffer.allocate(size)
        buffer.putShort(slots.size.toShort())

        for ((slotId, slot) in slots) {
            buffer.putShort(slotId.toShort())
            buffer.put(if (slot.isDeleted) 1.toByte() else 0.toByte())
            buffer.putShort(slot.length)
            buffer.put(slot.data)
        }
        return buffer.array()
    }

    companion object {
        fun deserialize(serializedData: ByteArray, pageId: PageId): SlottedPage {
            val buffer = ByteBuffer.wrap(serializedData)
            val slottedPage = SlottedPage(pageId, serializedData.size)
            val numSlots = buffer.short.toInt()

            repeat(numSlots) {
                val slotId = buffer.short.toInt()
                val isDeleted = buffer.get() == 1.toByte()
                val length = buffer.short
                val data = ByteArray(length.toInt())
                buffer.get(data)

                val slot = Slot(isDeleted, length, data)
                slottedPage.slots[slotId] = slot
            }

            return slottedPage
        }
    }
}