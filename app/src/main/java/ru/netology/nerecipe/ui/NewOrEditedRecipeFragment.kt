package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.showCategories
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.databinding.FragmentCreateBinding
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.dto.Recipe
import ru.netology.nerecipe.viewModel.RecipeViewModel

class NewOrEditedRecipeFragment : Fragment() {
    private val args by navArgs<NewOrEditedRecipeFragmentArgs>()
    private val newOrEditedRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCreateBinding.inflate(layoutInflater, container, false).also { binding ->

        val thisRecipe = args.currentRecipe
        if (thisRecipe != null) {
            with(binding) {
                title.setText(thisRecipe.title)
                authorName.setText(thisRecipe.authorName)
                textRecipe.setText(thisRecipe.textRecipe)
                categoryRecipeCheckBox.check(R.id.checkBoxEuropean)
                checkBoxEuropean.text = checkBoxEuropean.context.showCategories(Category.European)
                checkBoxAsian.text = checkBoxAsian.context.showCategories(Category.Asian)
                checkBoxPanasian.text = checkBoxPanasian.context.showCategories(Category.PanAsian)
                checkBoxEastern.text = checkBoxEastern.context.showCategories(Category.Eastern)
                checkBoxAmerican.text = checkBoxAmerican.context.showCategories(Category.American)
                checkBoxRussian.text = checkBoxRussian.context.showCategories(Category.Russian)
                checkBoxMediterranean.text = checkBoxMediterranean.context.showCategories(Category.Mediterranean)
            }
        }

        binding.title.requestFocus()

        binding.categoryRecipeCheckBox.setOnCheckedChangeListener { _, _ ->
            getCheckedCategory(binding.categoryRecipeCheckBox.checkedRadioButtonId)
        }
        binding.buttonSave.setOnClickListener {
            onButtonSaveClicked(binding)
        }
    }.root

    private fun onButtonSaveClicked(binding: FragmentCreateBinding) {
        val currentRecipe = Recipe(
            id = args.currentRecipe?.id ?: RecipeRepository.NEW_ID,
            title = binding.title.text.toString(),
            authorName = binding.authorName.text.toString(),
            textRecipe = binding.textRecipe.text.toString(),
            categoryRecipe = getCheckedCategory(binding.categoryRecipeCheckBox.checkedRadioButtonId)
        )
        if (emptyFieldsCheck(recipe = currentRecipe)) {
            val resultBundle = Bundle(1)
            resultBundle.putParcelable(RESULT_KEY, currentRecipe)
            setFragmentResult(REQUEST_KEY, resultBundle)
            findNavController().popBackStack()
        }
    }

    private fun getCheckedCategory(checkedId: Int) = when (checkedId) {
        R.id.checkBoxEuropean -> Category.European
        R.id.checkBoxAsian -> Category.Asian
        R.id.checkBoxPanasian -> Category.PanAsian
        R.id.checkBoxEastern -> Category.Eastern
        R.id.checkBoxAmerican -> Category.American
        R.id.checkBoxRussian -> Category.Russian
        R.id.checkBoxMediterranean -> Category.Mediterranean
        else -> throw IllegalArgumentException("Unknown type: $checkedId")
    }

    private fun emptyFieldsCheck(recipe: Recipe): Boolean {
        return if (recipe.title.isBlank() && recipe.authorName.isBlank() && recipe.textRecipe.isBlank()) {
            Toast.makeText(activity, "@string/all_filled", Toast.LENGTH_LONG).show()
            false
        } else true
    }

    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "newContent"
    }
}