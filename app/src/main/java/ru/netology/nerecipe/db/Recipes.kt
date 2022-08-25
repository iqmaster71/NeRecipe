package ru.netology.nerecipe.db

import ru.netology.nerecipe.dto.Recipe

fun RecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    authorName = authorName,
    categoryRecipe = categoryRecipe,
    textRecipe = textRecipe,
    isFavorite = isFavorite
)

fun Recipe.toEntity() = RecipeEntity(
    id = id,
    title = title,
    authorName = authorName,
    categoryRecipe = categoryRecipe,
    textRecipe = textRecipe,
    isFavorite = isFavorite
)