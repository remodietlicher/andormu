package ch.remodietlicher.andormu.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val DATA_STORE_NAME = "settings"

data class UserPreferences(val listOfTags: List<String>)

private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

class UserPreferenceRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    private object PreferencesKeys {
        val LIST_OF_TAGS = stringPreferencesKey("list_of_tags")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map {
        mapUserPreferences(it)
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val listOfTags = preferences[PreferencesKeys.LIST_OF_TAGS] ?: ""
        return UserPreferences(listOfTags.split(","))
    }


}
