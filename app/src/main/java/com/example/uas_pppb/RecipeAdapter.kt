package com.example.uas_pppb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_pppb.database.RecipeEntity
import com.example.uas_pppb.database.RecipeRoomDatabase
import com.example.uas_pppb.databinding.ItemRecipeBinding
import com.example.uas_pppb.model.Recipe
import com.bumptech.glide.Glide
import com.example.uas_pppb.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias OnClickRecipe = (Recipe) -> Unit

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onItemClick: OnClickRecipe,
    private val database: RecipeRoomDatabase
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            with(binding) {
                textViewRecipeName.text = recipe.name
                textViewCalories.text = "Calories: ${recipe.calories}"
                textViewDuration.text = "Cooking Time: ${recipe.cookingTime}"

                // Memuat gambar menggunakan Glide
                Glide.with(itemView.context)
                    .load(recipe.imageUrl)
                    .into(imageViewRecipe)

                // Cek status favorit dari database
                database.recipeDao().isRecipeFavorite(recipe.name).observeForever { isFavorite ->
                    buttonFavorite.setImageResource(
                        if (isFavorite) R.drawable.favorite
                        else R.drawable.unfavorite
                    )
                }

                // Handle save button click
                buttonFavorite.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.recipeDao()
                        val existingRecipe = dao.getRecipeByName(recipe.name)

                        if (existingRecipe != null) {
                            dao.delete(existingRecipe) // Hapus resep jika sudah ada
                        } else {
                            val recipeEntity = RecipeEntity(
                                name = recipe.name,
                                imageUrl = recipe.imageUrl,
                                calories = recipe.calories,
                                cookingTime = recipe.cookingTime
                            )
                            dao.insert(recipeEntity) // Simpan resep baru
                        }

                        // Update UI on the main thread
                        withContext(Dispatchers.Main) {
                            // Update button state if needed
                        }
                    }
                }

                // Handle item click
                itemView.setOnClickListener {
                    onItemClick(recipe) // Panggil callback untuk item click
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}
