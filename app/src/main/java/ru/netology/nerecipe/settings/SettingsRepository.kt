package ru.netology.nerecipe.settings

import ru.netology.nerecipe.dto.Category


interface SettingsRepository {

    fun saveStateSwitch(key: Category, b: Boolean)
    fun getStateSwitch(key: Category):Boolean
}