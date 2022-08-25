package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.dto.Recipe

interface RecipeInteractionListener {
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onFavoriteClicked(recipe: Recipe)
    fun onRecipeItemClicked(recipe: Recipe)
    fun onRecipeCardClicked(recipe: Recipe)
}