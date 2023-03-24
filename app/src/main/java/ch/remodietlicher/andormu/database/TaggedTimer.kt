package ch.remodietlicher.andormu.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomMasterTable.TABLE_NAME
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
