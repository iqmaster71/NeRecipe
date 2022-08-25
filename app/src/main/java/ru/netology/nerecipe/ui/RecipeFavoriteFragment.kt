package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.databinding.ListFavoriteBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RecipeFavoriteFragment : Fragment() {
    private val favoriteRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ListFavoriteBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = RecipesAdapter(favoriteRecipeViewModel)
        binding.listFavorite.adapter = adapter

        favoriteRecipeViewModel.data.observe(viewLifecycleOwner) { recipes ->
            val favoriteRecipes = recipes.filter { it.isFavorite }
            adapter.submitList(favoriteRecipes)

            val emptyList = recipes.none { it.isFavorite }
            binding.textBackground.visibility =
                if (emptyList) View.VISIBLE else View.GONE
        }

        favoriteRecipeViewModel.separateRecipeViewEvent.observe(viewLifecycleOwner) { recipeCardId ->
            val direction =
                RecipeFavoriteFragmentDirections.actionRecipeFavoriteFragmentToSeparateRecipeFragment(
                    recipeCardId
                )
            findNavController().navigate(direction)
        }
        favoriteRecipeViewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val direction =
                RecipeFavoriteFragmentDirections.actionRecipeFavoriteFragmentToNewOrEditedRecipeFragment(
                    recipe
                )
            findNavController().navigate(direction)
        }
    }.root

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(
            requestKey = NewOrEditedRecipeFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != NewOrEditedRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                NewOrEditedRecipeFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            favoriteRecipeViewModel.onSaveButtonClicked(newRecipe)
        }
    }
}