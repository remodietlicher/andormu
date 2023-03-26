package ch.remodietlicher.andormu

import android.content.Context
import androidx.room.Room
import ch.remodietlicher.andormu.data.AppDatabase
import ch.remodietlicher.andormu.data.TaggedTimer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

private const val DATABASE_NAME = "timer-database"



class TimerRepository @Inject constructor(@ApplicationContext context: Context) {
    private val database =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    fun getTimersAfterDate(date: Date) = database.timerDao().getTimersAfterDate(date)

    suspend fun insertTimer(timer: TaggedTimer) = database.timerDao().insert(timer)

    suspend fun deleteAllTimers() = database.timerDao().deleteAll()

    suspend fun update(newTimer: TaggedTimer) = database.timerDao().update(newTimer)
}
