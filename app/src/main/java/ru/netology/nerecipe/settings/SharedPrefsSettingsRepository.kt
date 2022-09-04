package ru.netology.nerecipe.settings

import android.app.Application
import android.content.Context
import ru.netology.nerecipe.dto.Category

class SharedPrefsSettingsRepository(
    application: Application
) : SettingsRepository {

    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)

    override fun saveStateSwitch(key: Category, b: Boolean) = prefs.edit().putBoolean(key.toString(), b).apply()
    override fun getStateSwitch(key: Category): Boolean = prefs.getBoolean(key.toString(), true)
}