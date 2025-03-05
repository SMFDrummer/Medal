package smf.talkweb.medal.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore by preferencesDataStore("settings")

inline fun <reified T> DataStore<Preferences>.read(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> = data
    .catch { emit(emptyPreferences()) }
    .map { it[key] ?: defaultValue }
    .flowOn(Dispatchers.IO)
    .conflate()

inline fun <reified T> DataStore<Preferences>.write(
    key: Preferences.Key<T>,
    value: T,
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) = scope.launch {
    edit { prefs ->
        prefs[key] = value
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getOrInit(
    key: Preferences.Key<T>,
    defaultValue: T
): T = withContext(Dispatchers.IO) {
    try {
        data.first()[key] ?: run {
            edit { it[key] = defaultValue }
            defaultValue
        }
    } catch (e: Exception) {
        defaultValue
    }
}