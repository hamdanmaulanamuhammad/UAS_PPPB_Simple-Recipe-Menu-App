package com.example.uas_pppb.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas_pppb.ActivityAuth
import com.example.uas_pppb.PrefManager
import com.example.uas_pppb.databinding.FragmentProfileBinding
import com.example.uas_pppb.model.Recipe
import com.example.uas_pppb.network.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var recipeAdapter: ProfileRecipeAdapter
    private val recipeList = mutableListOf<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Menampilkan sapaan pengguna
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "User") ?: "User"
        val email = sharedPreferences.getString("email", "Email") ?: "Email"
        binding.textViewName.text = name // Set username
        binding.textViewEmail.text = email

        // Setup RecyclerView
        setupRecyclerView()

        // Mengambil data resep dari API
        fetchUserRecipes()

        // Menangani klik tombol logout
        binding.buttonLogout.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = ProfileRecipeAdapter(recipeList) { recipe ->
            // Logika untuk menghapus resep dari API
            deleteRecipe(recipe)
        }
        binding.recyclerViewRecipes.adapter = recipeAdapter
    }

    private fun fetchUserRecipes() {
        val api = Client.getInstance()
        api.getRecipes().enqueue(object : Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        Log.d("ProfileFragment", "Recipes fetched: ${recipes.size}")
                        recipeList.clear()
                        recipeList.addAll(recipes)
                        recipeAdapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(requireContext(), "No recipes found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load recipes", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                Log.e("ProfileFragment", "Error fetching recipes: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching recipes: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteRecipe(recipe: Recipe) {
        val recipeId = recipe._id ?: return
        Log.d("ProfileFragment", "Deleting recipe with id: $recipeId")

        val api = Client.getInstance()
        api.deleteRecipe(recipeId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Hapus resep dari daftar dan perbarui UI
                    recipeList.remove(recipe)
                    recipeAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Recipe deleted: ${recipe.name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete recipe", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileFragment", "Error deleting recipe: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ProfileFragment", "Error deleting recipe: ${t.message}")
                Toast.makeText(requireContext(), "Error deleting recipe: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        // Mengatur status login ke false
        val prefManager = PrefManager.getInstance(requireContext())
        prefManager.saveLoginStatus(false)

        // Kembali ke layar login
        val intent = Intent(requireContext(), ActivityAuth::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        requireActivity().finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Membersihkan binding untuk mencegah kebocoran memori
    }
}

