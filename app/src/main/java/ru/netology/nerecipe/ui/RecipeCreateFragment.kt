package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.databinding.FragmentFilterBinding
import ru.netology.nerecipe.dto.Category
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RecipeCreateFragment : Fragment() {
    private val recipeCreateViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFilterBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            checkBoxEuropean.isChecked = recipeCreateViewModel.getStateSwitch(Category.European)
            checkBoxAsian.isChecked = recipeCreateViewModel.getStateSwitch(Category.Asian)
            checkBoxPanasian.isChecked = recipeCreateViewModel.getStateSwitch(Category.PanAsian)
            checkBoxEastern.isChecked = recipeCreateViewModel.getStateSwitch(Category.Eastern)
            checkBoxAmerican.isChecked = recipeCreateViewModel.getStateSwitch(Category.American)
            checkBoxRussian.isChecked = recipeCreateViewModel.getStateSwitch(Category.Russian)
            checkBoxMediterranean.isChecked = recipeCreateViewModel.getStateSwitch(Category.Mediterranean)

            with(binding) {

                checkBoxEuropean.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.European,
                        checkBoxEuropean.isChecked
                    )
                }
                checkBoxAsian.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.Asian,
                        checkBoxAsian.isChecked
                    )
                }
                checkBoxPanasian.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.PanAsian,
                        checkBoxPanasian.isChecked
                    )
                }
                checkBoxEastern.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.Eastern,
                        checkBoxEastern.isChecked
                    )
                }
                checkBoxAmerican.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.American,
                        checkBoxAmerican.isChecked
                    )
                }
                checkBoxRussian.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.Russian,
                        checkBoxRussian.isChecked
                    )
                }
                checkBoxMediterranean.setOnClickListener {
                    recipeCreateViewModel.saveStateSwitch(
                        Category.Mediterranean,
                        checkBoxMediterranean.isChecked
                    )
                }
            }

            binding.buttonSave.setOnClickListener {
                onButtonSaveClicked(binding)
            }
        }
    }.root

    private fun onButtonSaveClicked(binding: FragmentFilterBinding) {
        val categoryList = arrayListOf<Category>()
        var checkedCount = 7
        val nothingIsChecked = 0

        if (binding.checkBoxEuropean.isChecked) {
            categoryList.add(Category.European)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxAsian.isChecked) {
            categoryList.add(Category.Asian)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxPanasian.isChecked) {
            categoryList.add(Category.PanAsian)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxEastern.isChecked) {
            categoryList.add(Category.Eastern)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxAmerican.isChecked) {
            categoryList.add(Category.American)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxRussian.isChecked) {
            categoryList.add(Category.Russian)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (binding.checkBoxMediterranean.isChecked) {
            categoryList.add(Category.Mediterranean)
            recipeCreateViewModel.setCategoryFilter = true
        } else {
            --checkedCount
        }

        if (checkedCount == nothingIsChecked) {
            Toast.makeText(activity, "You cannot disable all filters", Toast.LENGTH_LONG).show()
        } else {
            recipeCreateViewModel.showRecipesByCategories(categoryList)
            val resultBundle = Bundle(1)
            resultBundle.putParcelableArrayList(CHECKBOX_KEY, categoryList)
            setFragmentResult(CHECKBOX_KEY, resultBundle)
            findNavController().popBackStack()
        }
    }

    companion object {
        const val CHECKBOX_KEY = "checkBoxContent"
    }
}