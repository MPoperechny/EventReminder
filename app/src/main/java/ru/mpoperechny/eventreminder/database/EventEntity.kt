package ru.mpoperechny.eventreminder.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class EventEntity(
    var date: Long,
    var type: Int,
    var person: String?,
    var description: String?
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @delegate:Ignore
    val daysLeft: Int by lazy { daysLeft(date) }

    //@Ignore
    //val mark = println("created EventEntity ${this.hashCode()}")

    companion object {
        const val BIRTHDAY = 0
        const val HOLIDAY = 1
        const val OTHER = 2
    }

    object Values{
        const val DATE_UNDEFINED_VALUE = Long.MIN_VALUE
    }

    private fun daysLeft(timeInMilliseconds: Long): Int {

        //println("calculate daysLeft for ${this.hashCode()}")

        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)
        val todayDayOfYear = today.get(Calendar.DAY_OF_YEAR)

        val targetDay = Calendar.getInstance().apply {
            timeInMillis = timeInMilliseconds
            set(Calendar.YEAR, currentYear)
        }
        val targetDayThisYear = targetDay.get(Calendar.DAY_OF_YEAR)

        return when {
            targetDayThisYear == todayDayOfYear -> 0
            targetDay.after(today) -> targetDayThisYear - todayDayOfYear
            else -> {
                val targetDayNextYear = targetDay.apply { add(Calendar.YEAR, 1); }
                    .get(Calendar.DAY_OF_YEAR)
                val lastDayOfCurrentYear = Calendar.getInstance().apply { set(currentYear, 11, 31) }
                    .get(Calendar.DAY_OF_YEAR)

                lastDayOfCurrentYear - todayDayOfYear + targetDayNextYear
            }
        }

    }
}
