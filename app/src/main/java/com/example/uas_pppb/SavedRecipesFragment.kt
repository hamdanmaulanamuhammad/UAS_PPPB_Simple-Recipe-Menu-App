package com.example.uas_pppb.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uas_pppb.database.RecipeRoomDatabase
import com.example.uas_pppb.databinding.FragmentSavedRecipesBinding
import com.example.uas_pppb.database.RecipeEntity
import com.example.uas_pppb.model.Recipe

class SavedRecipesFragment : Fragment() {

    private lateinit var binding: FragmentSavedRecipesBinding
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<Recipe>() // Daftar resep yang disimpan
    private lateinit var database: RecipeRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)

        // Initialize database
        database = RecipeRoomDatabase.getDatabase(requireContext())

        // Setup RecyclerView
        setupRecyclerView()

        // Mengambil data resep yang disimpan dari database
        fetchSavedRecipes()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSavedRecipes.layoutManager = GridLayoutManager(requireContext(), 2)
        recipeAdapter = RecipeAdapter(recipeList, { recipe ->
            // Logika untuk menangani klik item resep
            // Misalnya, menampilkan detail resep
        }, database) // Pass database to adapter
        binding.recyclerViewSavedRecipes.adapter = recipeAdapter
    }

    private fun fetchSavedRecipes() {
        // Mengambil semua resep yang disimpan dari database
        database.recipeDao().getAllSavedRecipes().observe(viewLifecycleOwner) { recipeEntities ->
            recipeEntities?.let {
                // Konversi RecipeEntity ke Recipe
                recipeList.clear()
                recipeList.addAll(it.map { entity ->
                    Recipe(
                        name = entity.name,
                        imageUrl = entity.imageUrl,
                        calories = entity.calories,
                        cookingTime = entity.cookingTime
                    )
                })
                recipeAdapter.notifyDataSetChanged() // Update adapter
            } ?: run {
                Log.e("SavedRecipesFragment", "No saved recipes found")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}
