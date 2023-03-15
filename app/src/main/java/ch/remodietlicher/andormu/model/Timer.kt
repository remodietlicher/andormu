package ch.remodietlicher.andormu.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Timer(
    @PrimaryKey val id: Int,
    val startTime: Date,
    val endTime: Date,
    val tags: List<String>
)
