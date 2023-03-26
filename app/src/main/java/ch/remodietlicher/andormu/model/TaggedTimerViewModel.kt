package ch.remodietlicher.andormu.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.remodietlicher.andormu.TimerRepository
import ch.remodietlicher.andormu.data.TaggedTimer
import ch.remodietlicher.andormu.data.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class TaggedTimerViewModel
@Inject
constructor(
    private val timerRepository: TimerRepository,
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModel() {
    private var _timers: MutableStateFlow<List<TaggedTimer>> = MutableStateFlow(emptyList())
    val timers: StateFlow<List<TaggedTimer>>
        get() = _timers.asStateFlow()

    val activeTimerTags: Flow<List<String>>
        get() =
            _timers.map { timers -> timers.filter { it.endTime == null }.map { it.tag }.distinct() }

    init {
        viewModelScope.launch {
            timerRepository.getTimersAfterDate(getBeginningOfDay()).collect { _timers.value = it }
        }
    }

    fun toggleTimer(tag: String): Boolean {
        val mostRecentTimer = _timers.value.filter { it.tag == tag }.maxByOrNull { it.startTime }

        val timerIsActive =
            if (mostRecentTimer == null || mostRecentTimer.endTime != null) {
                val newTimer = TaggedTimer(UUID.randomUUID(), Date(), null, tag)
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
