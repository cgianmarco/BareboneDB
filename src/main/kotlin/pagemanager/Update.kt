package pagemanager

import Slot
import SlotId

data class Update(
    val id: SlotId,
    val before: Slot?,
    val after: Slot?
)