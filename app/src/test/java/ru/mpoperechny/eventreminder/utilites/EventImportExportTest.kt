package ru.mpoperechny.eventreminder.utilites

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.mpoperechny.eventreminder.database.EventEntity

class EventImportExportTest {
    @Test
    fun roundTrip_serializesAndDeserializesEvents() {
        val events = listOf(
            EventEntity(1700000000000L, EventEntity.BIRTHDAY, "Alice", "Birthday"),
            EventEntity(1710000000000L, EventEntity.OTHER, "Bob", "Meeting")
        )
        events[0].id = 15
        events[1].id = 28

        val json = EventImportExport.toJson(events)
        val restored = EventImportExport.fromJson(json)

        assertEquals(events.size, restored.size)
        assertEquals(events[0].id, restored[0].id)
        assertEquals(events[0].date, restored[0].date)
        assertEquals(events[0].type, restored[0].type)
        assertEquals(events[0].person, restored[0].person)
        assertEquals(events[0].description, restored[0].description)
        assertEquals(events[1].id, restored[1].id)
        assertEquals(events[1].person, restored[1].person)
    }
}
