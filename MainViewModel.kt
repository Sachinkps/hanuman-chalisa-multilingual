package com.sachinkps.hanumanChalisa.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sachinkps.hanumanChalisa.data.models.SUPPORTED_LANGUAGES
import com.sachinkps.hanumanChalisa.data.models.VerseRaw
import com.sachinkps.hanumanChalisa.data.repository.ChaLisaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val android.content.Context.dataStore by preferencesDataStore(name = "settings")

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChaLisaRepository(application)
    private val LANG_KEY = stringPreferencesKey("selected_language")

    private val _selectedLangCode = MutableStateFlow("hi")
    val selectedLangCode: StateFlow<String> = _selectedLangCode.asStateFlow()

    private val _showTransliteration = MutableStateFlow(true)
    val showTransliteration: StateFlow<Boolean> = _showTransliteration.asStateFlow()

    private val _verses = MutableStateFlow<List<VerseRaw>>(emptyList())
    val verses: StateFlow<List<VerseRaw>> = _verses.asStateFlow()

    private val _title = MutableStateFlow("हनुमान चालीसा")
    val title: StateFlow<String> = _title.asStateFlow()

    init {
        loadPreferences()
        loadVerses()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            getApplication<Application>().dataStore.data
                .map { prefs -> prefs[LANG_KEY] ?: "hi" }
                .collect { lang ->
                    _selectedLangCode.value = lang
                    updateTitle(lang)
                }
        }
    }

    private fun loadVerses() {
        viewModelScope.launch {
            _verses.value = repository.getAllVerses()
        }
    }

    fun setLanguage(langCode: String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[LANG_KEY] = langCode
            }
            _selectedLangCode.value = langCode
            updateTitle(langCode)
        }
    }

    fun toggleTransliteration() {
        _showTransliteration.value = !_showTransliteration.value
    }

    private fun updateTitle(langCode: String) {
        _title.value = repository.getTitle(langCode)
    }

    val supportedLanguages = SUPPORTED_LANGUAGES
}
