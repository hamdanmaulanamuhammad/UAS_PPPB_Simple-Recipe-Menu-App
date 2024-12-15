package com.example.uas_pppb.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uas_pppb.database.RecipeRoomDatabase
import com.example.uas_pppb.databinding.FragmentHomeBinding
import com.example.uas_pppb.model.Recipe
import com.example.uas_pppb.network.Client
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<Recipe>() // Daftar resep
    private lateinit var database: RecipeRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Menampilkan sapaan pengguna
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "User") ?: "User"
        binding.textViewUsername.text = name // Set username

        // Menentukan sapaan berdasarkan waktu
        val greeting = getGreeting()
        binding.textViewGreeting.text = greeting

        // Initialize database
        database = RecipeRoomDatabase.getDatabase(requireContext())

        // Setup RecyclerView
        setupRecyclerView()

        // Mengambil data resep dari API
        fetchRecipes()

        return binding.root
    }

    private fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 0..11 -> "Good Morning"
            hour in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRecipes.layoutManager = GridLayoutManager(requireContext(), 2)
        recipeAdapter = RecipeAdapter(recipeList, { recipe ->
            // toast saat klik item menu resep
            Toast.makeText(requireContext(), "Clicked: ${recipe.name}", Toast.LENGTH_SHORT).show()
        }, database) // Pass database to adapter
        binding.recyclerViewRecipes.adapter = recipeAdapter
    }

    private fun fetchRecipes() {
        val api = Client.getInstance()
        api.getRecipes().enqueue(object : Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        // Update adapter dengan daftar resep yang diambil
                        recipeList.clear()
                        recipeList.addAll(recipes)
                        recipeAdapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(requireContext(), "No recipes found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load recipes", Toast.LENGTH_SHORT).show()
                    Log.e("HomeFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching recipes: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching recipes: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Membersihkan binding untuk mencegah kebocoran memori
    }
}
