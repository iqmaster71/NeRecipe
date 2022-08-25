package ru.netology.nerecipe.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Recipe(
    val id: Long,
    val title: String,
    val authorName: String,
    val categoryRecipe: Category,
    val textRecipe: String,
    val isFavorite: Boolean = false
) : Parcelable

@Serializable
@Parcelize
enum class Category : Parcelable {
    European,
    Asian,
    PanAsian,
    Eastern,
    American,
    Russian,
    Mediterranean
}