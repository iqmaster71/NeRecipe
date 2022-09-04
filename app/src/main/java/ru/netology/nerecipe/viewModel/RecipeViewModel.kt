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
import ru.netology.nerecipe.settings.SettingsRepository
import ru.netology.nerecipe.settings.SharedPrefsSettingsRepository
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
    private val currentRecipe = MutableLiveData<Recipe?>(null)
    private var favoriteFilter: MutableLiveData<Boolean> = MutableLiveData()
    private val repositorySettings: SettingsRepository = SharedPrefsSettingsRepository(application)
    private val categoryList = arrayListOf<Category>()

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

    override fun saveStateSwitch(key: Category, b: Boolean) {
        setupCategories(key, b)
        repositorySettings.saveStateSwitch(key, b)
    }

    override fun getStateSwitch(key: Category): Boolean {
        val b = repositorySettings.getStateSwitch(key)
        setupCategories(key, b)
        return b
    }

    private fun setupCategories(key: Category, b: Boolean) {
        if (key == Category.European) {
            if (b) {
                categoryList.add(Category.European)
            } else {
                categoryList.remove(Category.European)
            }
        }
        if (key == Category.Asian) {
            if (b) {
                categoryList.add(Category.Asian)
            } else {
                categoryList.remove(Category.Asian)
            }
        }
        if (key == Category.PanAsian) {
            if (b) {
                categoryList.add(Category.PanAsian)
            } else {
                categoryList.remove(Category.PanAsian)
            }
        }
        if (key == Category.Eastern) {
            if (b) {
                categoryList.add(Category.Eastern)
            } else {
                categoryList.remove(Category.Eastern)
            }
        }
        if (key == Category.American) {
            if (b) {
                categoryList.add(Category.American)
            } else {
                categoryList.remove(Category.American)
            }
        }
        if (key == Category.Russian) {
            if (b) {
                categoryList.add(Category.Russian)
            } else {
                categoryList.remove(Category.Russian)
            }
        }
        if (key == Category.Mediterranean) {
            if (b) {
                categoryList.add(Category.Mediterranean)
            } else {
                categoryList.remove(Category.Mediterranean)
            }
        }
        repository.setFilter(categoryList)
    }

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