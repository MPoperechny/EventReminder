package ru.mpoperechny.eventreminder.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import java.util.concurrent.Executors


@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)

abstract class EventsDatabase : RoomDatabase() {
    abstract val eventsDao: EventsDAO
    private val context: Context? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: EventsDatabase? = null

        fun getDatabase(context: Context): EventsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventsDatabase::class.java,
                    "events.db"
                )
                    .addCallback(sRoomDatabaseCallback)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }


        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadScheduledExecutor().execute {
                    //Log.d("rlf_app", "set default");
                    INSTANCE!!.eventsDao.deleteAll()
                    //INSTANCE!!.eventsDao.insertEvents(*EventsDatabase.defaultData)
                }
            }
        }

        internal val defaultData: Array<EventEntity>
            get() = arrayOf(
                EventEntity(1, EventEntity.BIRTHDAY, "WWWW", "wwww description"),
                EventEntity(2, EventEntity.BIRTHDAY, "EEEE", "eeee description")
            )
    }
}
