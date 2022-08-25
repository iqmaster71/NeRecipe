package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.RoomRecipeRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: RecipeRepository = RoomRecipeRepositoryImpl(
        dao = AppDb.getInstance(context = application).recipeDao
    )

    private var categoriesFilter: List<Category> = Category.values().toList()

    var setCategoryFilter = false

    val data = repository.data.map { list ->
        list.filter { categoriesFilter.contains(it.categoryRecipe) }
    }

    val separateRecipeViewEvent = SingleLiveEvent<Long>()
    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe?>()
    val currentRecipe = MutableLiveData<Recipe?>(null)
    var favoriteFilter: MutableLiveData<Boolean> = MutableLiveData()

    init {
        favoriteFilter.value = false
    }

    fun showRecipesByCategories(categories: List<Category>) {
        categoriesFilter = categories
        repository.update()
    }

    fun onSaveButtonClicked(recipe: Recipe) {
        if (recipe.textRecipe.isBlank() && recipe.title.isBlank()) return
        val newRecipe = currentRecipe.value?.copy(
            title = recipe.title,
            authorName = recipe.authorName,
            textRecipe = recipe.textRecipe,
            categoryRecipe = recipe.categoryRecipe
        ) ?: Recipe(
            id = RecipeRepository.NEW_ID,
            title = recipe.title,
            authorName = recipe.authorName,
            categoryRecipe = recipe.categoryRecipe,
            textRecipe = recipe.textRecipe
        )
        repository.save(newRecipe)
        currentRecipe.value = null
    }

    fun onAddButtonClicked() {
        navigateToRecipeContentScreenEvent.call()
    }

    fun searchRecipeByName(recipeName: String) = repository.search(recipeName)

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value = recipe
    }

    override fun onRecipeCardClicked(recipe: Recipe) {
        separateRecipeViewEvent.value = recipe.id
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.toFavorite(recipe.id)
    override fun onRecipeItemClicked(recipe: Recipe) {
        navigateToRecipeContentScreenEvent.value = recipe
    }
}