package com.example.uas_pppb.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recipeEntity: RecipeEntity)

    @Delete
    fun delete(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM saved_recipes ORDER BY id ASC")
    fun getAllRecipes(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM saved_recipes")
    fun getAllSavedRecipes(): LiveData<List<RecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_recipes WHERE name = :name)")
    fun isRecipeFavorite(name: String): LiveData<Boolean>

    @Query("SELECT * FROM saved_recipes WHERE name = :name LIMIT 1")
    fun getRecipeByName(name: String): RecipeEntity?
}
