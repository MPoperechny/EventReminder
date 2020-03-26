package ru.mpoperechny.eventreminder.database

import androidx.lifecycle.LiveData
import androidx.room.*

import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface EventsDAO {


    @get:Query("SELECT * FROM events")
    val allEvents: LiveData<List<EventEntity>>

    @Query("SELECT * FROM events")
    suspend fun getAll(): List<EventEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Update
    fun updateEvent(event: EventEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertEvents(vararg events: EventEntity)

    @Delete
    suspend fun deleteEvents(vararg events: EventEntity)

    @Query("DELETE FROM events")
    suspend fun deleteAll()

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    fun getEvent(id: Int): LiveData<EventEntity>

    /*
    @Query("SELECT COUNT(*) FROM events")
    int getEventsTotal();
    */

    /*
    @Query("SELECT eventName FROM events WHERE eventId = :eventId LIMIT 1")
    LiveData<String> loadEventName(int eventId);
    */
}
