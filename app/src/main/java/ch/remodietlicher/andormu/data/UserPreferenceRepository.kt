package ch.remodietlicher.andormu.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "settings"

data class UserPreferences(val listOfTags: Set<String>)

private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

class UserPreferenceRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    private object PreferencesKeys {
        val LIST_OF_TAGS = stringPreferencesKey("list_of_tags")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { mapUserPreferences(it) }

    suspend fun addTag(tag: String) {
        val currentPreferences = dataStore.data.first()
        val listOfTags = getListOfTagsFromPreferences(currentPreferences)
        val newListOfTags = listOfTags + tag
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIST_OF_TAGS] = newListOfTags.joinToString(",")
        }
    }

    suspend fun removeTag(tag: String) {
        val currentPreferences = dataStore.data.first()
        val setOfTags =
            currentPreferences[PreferencesKeys.LIST_OF_TAGS]?.split(",")?.toSet() ?: emptySet()
        val newSetOfTags = setOfTags - tag
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIST_OF_TAGS] = newSetOfTags.joinToString(",")
        }
    }

    private fun mapUserPreferences(preferences: Preferences) =
        UserPreferences(getListOfTagsFromPreferences(preferences))

    private fun getListOfTagsFromPreferences(preferences: Preferences): Set<String> {
        val tagsString: String = preferences[PreferencesKeys.LIST_OF_TAGS] ?: ""
        val listOfTags =
            if (tagsString.isEmpty()) {
                emptySet()
            } else {
                tagsString.split(",").toSet()
            }

        return listOfTags
    }
}
