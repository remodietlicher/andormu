package ch.remodietlicher.andormu

import android.content.Context
import androidx.room.Room
import ch.remodietlicher.andormu.database.AppDatabase
import ch.remodietlicher.andormu.database.TaggedTimer
import kotlinx.coroutines.flow.Flow

private const val DATABASE_NAME = "timer-database"

class TimerRepository private constructor(context: Context) {
    private val database =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .build()

    fun getTimers(): Flow<List<TaggedTimer>> = database.timerDao().getAll()

    suspend fun getTimer(id: Int) = database.timerDao().loadById(id)

    suspend fun getTimersByTags(tags: List<String>) = database.timerDao().loadAllByTags(tags)

    suspend fun getMostRecentTimerByTag(tags: String) =
        database.timerDao().loadMostRecentByTags(tags)

    suspend fun insertTimer(timer: TaggedTimer) = database.timerDao().insert(timer)

    suspend fun deleteTimer(timer: TaggedTimer) = database.timerDao().delete(timer)

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
