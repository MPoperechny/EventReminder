package ru.mpoperechny.eventreminder.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

import java.util.concurrent.Executors


@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)

abstract class EventsDatabase : RoomDatabase() {
    abstract val eventsDao: EventsDAO

    companion object {

        @Volatile
        private var INSTANCE: EventsDatabase? = null

        fun getDatabase(context: Context): EventsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventsDatabase::class.java,
                    "events.db"
                )
                    .addCallback(EventsDatabaseCallback(GlobalScope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class EventsDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database.eventsDao.deleteAll()
                        database.eventsDao.insertEvents(*EventsDatabase.defaultData)
                    }
                }
            }
        }

        internal val defaultData: Array<EventEntity>
            get() = arrayOf(

                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Иванович",
                    "описание1"
                ),
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Иванович",
                    "описание1"
                ),
                EventEntity(
                    GregorianCalendar(1965, Calendar.FEBRUARY, 18).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Иванов Иван Иванович",
                    "описание1"
                ),
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )
                ,
                EventEntity(
                    GregorianCalendar(1970, Calendar.MARCH, 1).timeInMillis,
                    EventEntity.BIRTHDAY,
                    "Петров Петр Петрович",
                    "описание2"
                )

            )
    }
}
