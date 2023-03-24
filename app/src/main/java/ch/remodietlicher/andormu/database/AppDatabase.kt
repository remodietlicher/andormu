package ch.remodietlicher.andormu.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaggedTimer::class], version = 2)
@TypeConverters(TimerTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerDao(): TimerDao
}
