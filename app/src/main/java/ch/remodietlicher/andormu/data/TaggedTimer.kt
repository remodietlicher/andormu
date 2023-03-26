package ch.remodietlicher.andormu.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

const val TIMER_TABLE_NAME = "timer"

@Entity(tableName = TIMER_TABLE_NAME)
data class TaggedTimer(
    @PrimaryKey val id: UUID,
    val startTime: Date,
    val endTime: Date?,
    val tag: String
)
