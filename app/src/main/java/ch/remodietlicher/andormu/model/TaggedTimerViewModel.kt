package ch.remodietlicher.andormu.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.remodietlicher.andormu.TimerRepository
import ch.remodietlicher.andormu.database.TaggedTimer
import java.util.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaggedTimerViewModel : ViewModel() {
    private val timerRepository = TimerRepository.get()

    private var _timers: MutableStateFlow<List<TaggedTimer>> = MutableStateFlow(emptyList())
    val timers: StateFlow<List<TaggedTimer>>
        get() = _timers.asStateFlow()

    init {
        viewModelScope.launch {
            timerRepository.getTimersAfterDate(getBeginningOfDay()).collect { _timers.value = it }
        }
    }

    fun toggleTimer(tag: String): Boolean {
        val mostRecentTimer =
            _timers.value.filter { it.tags.contains(tag) }.maxByOrNull { it.startTime }

        val timerIsActive =
            if (mostRecentTimer == null || mostRecentTimer.endTime != null) {
                val newTimer = TaggedTimer(UUID.randomUUID(), Date(), null, listOf(tag))
                viewModelScope.launch { timerRepository.insertTimer(newTimer) }
                _timers.update { it + newTimer }
                true
            } else {
                val newTimer = mostRecentTimer.copy(endTime = Date())
                viewModelScope.launch { timerRepository.update(newTimer) }
                _timers.update {
                    it.map { timer -> if (timer.id == newTimer.id) newTimer else timer }
                }
                false
            }

        return timerIsActive
    }

    suspend fun deleteAllTimers() = timerRepository.deleteAllTimers()

    fun getBeginningOfDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getBeginningOfHour(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getEndOfHour(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

    fun getEndOfDay(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
}
