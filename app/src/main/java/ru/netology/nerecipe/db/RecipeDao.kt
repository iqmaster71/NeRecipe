package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.dto.Category

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipeEntity)

    @Query("UPDATE recipes SET title = :title, authorName = :authorName, textRecipe = :textRecipe, categoryRecipe = :categoryRecipe WHERE id = :id")
    fun updateById(
        id: Long, title: String, authorName: String,
        textRecipe: String, categoryRecipe: Category
    )

    fun save(recipe: RecipeEntity) =
        if (recipe.id == RecipeRepository.NEW_ID)
            insert(recipe)
        else updateById(recipe.id, recipe.title, recipe.authorName, recipe.textRecipe, recipe.categoryRecipe)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun delete(id: Long)

    @Query(
        """
        UPDATE recipes SET
        isFavorite = CASE WHEN isFavorite THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun toFavorite(id: Long)

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :text || '%'")
    fun search(text: String): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE categoryRecipe = :categoryRecipe")
    fun getCategory(categoryRecipe: Category): LiveData<List<RecipeEntity>>

    @Query("UPDATE recipes SET title = title")
    fun update()
}