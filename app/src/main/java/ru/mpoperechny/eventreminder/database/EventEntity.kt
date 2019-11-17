package ru.mpoperechny.eventreminder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventEntity(var date: Int, var type: Int, var person: String?, var description: String?) {


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        var BIRTHDAY = 0
        var HOLIDAY = 1
    }
}
