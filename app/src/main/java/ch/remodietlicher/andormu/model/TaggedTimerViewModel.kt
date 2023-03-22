package ch.remodietlicher.andormu.model

import androidx.lifecycle.ViewModel
import ch.remodietlicher.andormu.TimerRepository
import ch.remodietlicher.andormu.database.TaggedTimer
import java.util.*

class TaggedTimerViewModel : ViewModel() {
    private val timerRepository = TimerRepository.get()

    suspend fun toggleTimer(tag: String): Boolean {
        val mostRecentTimer = timerRepository.getMostRecentTimerByTag(tag)

        val timerIsActive =
            if (mostRecentTimer == null || mostRecentTimer.endTime != null) {
                val newTimer = TaggedTimer(UUID.randomUUID(), Date(), null, listOf(tag))
                timerRepository.insertTimer(newTimer)
                true
            } else {
                val newTimer = mostRecentTimer.copy(endTime = Date())
                timerRepository.update(newTimer)
                false
            }

        return timerIsActive
    }

    suspend fun deleteAllTimers() = timerRepository.deleteAllTimers()

    suspend fun getAllTimers() = timerRepository.getTimers()
}
