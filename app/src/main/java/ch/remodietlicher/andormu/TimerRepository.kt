package ch.remodietlicher.andormu

import android.content.Context
import androidx.room.Room
import ch.remodietlicher.andormu.database.AppDatabase
import ch.remodietlicher.andormu.database.TaggedTimer
import java.util.*

private const val DATABASE_NAME = "timer-database"

class TimerRepository private constructor(context: Context) {
    private val database =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()

    fun getTimersAfterDate(date: Date) = database.timerDao().getTimersAfterDate(date)

    suspend fun insertTimer(timer: TaggedTimer) = database.timerDao().insert(timer)

    suspend fun deleteAllTimers() = database.timerDao().deleteAll()

    suspend fun update(newTimer: TaggedTimer) = database.timerDao().update(newTimer)

    companion object {
        private var instance: TimerRepository? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = TimerRepository(context)
            }
        }

        fun get(): TimerRepository {
            return instance ?: throw IllegalStateException("TimerRepository must be initialized")
        }
    }
}
