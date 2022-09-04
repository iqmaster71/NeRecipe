package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Recipe

interface RecipeInteractionListener {
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onFavoriteClicked(recipe: Recipe)
    fun onRecipeItemClicked(recipe: Recipe)
    fun onRecipeCardClicked(recipe: Recipe)
    fun saveStateSwitch(key: Category, b: Boolean)
    fun getStateSwitch(key: Category): Boolean
}