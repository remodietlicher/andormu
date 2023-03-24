package ch.remodietlicher.andormu.database

import androidx.room.*
import java.util.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query("SELECT * FROM $TIMER_TABLE_NAME") fun getAll(): Flow<List<TaggedTimer>>

    // get timers with start or end time later than a given date
    @Query("SELECT * FROM $TIMER_TABLE_NAME WHERE startTime > :date OR endTime > :date")
    fun getTimersAfterDate(date: Date): Flow<List<TaggedTimer>>

    @Insert suspend fun insert(timers: TaggedTimer)

    @Update suspend fun update(timer: TaggedTimer)

    // delete all timers
    @Query("DELETE FROM $TIMER_TABLE_NAME") suspend fun deleteAll()
}
