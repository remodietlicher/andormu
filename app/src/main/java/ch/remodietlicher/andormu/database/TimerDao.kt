package ch.remodietlicher.andormu.database

import androidx.room.*
import java.util.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query("SELECT * FROM $TIMER_TABLE_NAME") fun getAll(): Flow<List<TaggedTimer>>

    @Query("SELECT * FROM $TIMER_TABLE_NAME WHERE id = :id")
    suspend fun loadById(id: Int): TaggedTimer

    @Query("SELECT * FROM $TIMER_TABLE_NAME WHERE tags IN (:tags)")
    suspend fun loadAllByTags(tags: List<String>): List<TaggedTimer>

    @Query("SELECT * FROM $TIMER_TABLE_NAME WHERE tags IN (:tags) ORDER BY startTime DESC LIMIT 1")
    suspend fun loadMostRecentByTags(tags: String): TaggedTimer?

    @Insert suspend fun insert(timers: TaggedTimer)

    @Update suspend fun update(timer: TaggedTimer)

    @Delete suspend fun delete(timer: TaggedTimer)

    // delete all timers
    @Query("DELETE FROM $TIMER_TABLE_NAME") suspend fun deleteAll()
}
