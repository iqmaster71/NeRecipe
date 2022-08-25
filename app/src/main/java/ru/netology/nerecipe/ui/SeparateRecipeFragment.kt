package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.databinding.SeparateRecipeViewBinding
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.viewModel.RecipeViewModel

class SeparateRecipeFragment : Fragment() {
    private val args by navArgs<SeparateRecipeFragmentArgs>()
    private val separateRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SeparateRecipeViewBinding.inflate(layoutInflater, container, false).also { binding ->
        val viewHolder =
            RecipesAdapter.ViewHolder(binding.separateRecipeView, separateRecipeViewModel)
        separateRecipeViewModel.data.observe(viewLifecycleOwner) { recipes ->
            val separatedRecipe = recipes.find { it.id == args.recipeCardId } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(separatedRecipe)
            binding.textRecipe.text = separatedRecipe.textRecipe
        }

        separateRecipeViewModel.navigateToRecipeContentScreenEvent.observe(viewLifecycleOwner) { recipe ->
            val direction =
                SeparateRecipeFragmentDirections.actionSeparateRecipeFragmentToNewOrEditedRecipeFragment(
                    recipe
                )
            findNavController().navigate(direction)
        }

        setFragmentResultListener(
            requestKey = NewOrEditedRecipeFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != NewOrEditedRecipeFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                NewOrEditedRecipeFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            separateRecipeViewModel.onSaveButtonClicked(newRecipe)
        }
    }.root
}