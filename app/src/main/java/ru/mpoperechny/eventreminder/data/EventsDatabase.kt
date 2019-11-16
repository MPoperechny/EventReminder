package ru.mpoperechny.eventreminder.data

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
/*, AnotherEntityType.class, AThirdEntityType.class */
abstract class EventsDatabase : RoomDatabase() {
    abstract val eventsDao: EventsDAO
    private val context: Context? = null

    companion object {

        private var eventsDatabase: EventsDatabase? = null

        fun getInstance(context: Context): EventsDatabase? {
            if (eventsDatabase == null) {
                synchronized(EventsDatabase::class) {
                    if (eventsDatabase == null) {
                        eventsDatabase = Room.databaseBuilder(
                            context.applicationContext,
                            EventsDatabase::class.java,
                            "database"
                        )
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return eventsDatabase
        }


        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadScheduledExecutor().execute {
                    //Log.d("rlf_app", "set default");
                    eventsDatabase!!.eventsDao.insertEvents(*EventsDatabase.defaultData)
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
