package com.example.uas_pppb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.uas_pppb.R
import com.example.uas_pppb.databinding.FragmentAddRecipeBinding
import com.example.uas_pppb.model.Recipe
import com.example.uas_pppb.network.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

        // Menangani penambahan resep
        binding.buttonAddRecipe.setOnClickListener {
            addRecipe()
        }

        // Menangani perubahan pada EditText URL Gambar
        binding.editTextImageUrl.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                loadImageFromUrl(binding.editTextImageUrl.text.toString())
            }
        }

        return binding.root
    }

    private fun loadImageFromUrl(url: String) {
        if (url.isNotEmpty()) {
            // Menggunakan Glide untuk memuat gambar
            Glide.with(this)
                .load(url)
                .error(R.drawable.placeholder_image_recipe) // Gambar jika gagal
                .into(binding.imageViewRecipe)
        }
    }

    private fun addRecipe() {
        val recipeName = binding.editTextRecipeName.text.toString()
        val calories = binding.editTextCalories.text.toString().toIntOrNull() ?: 0
        val duration = binding.editTextDuration.text.toString()
        val imageUrl = binding.editTextImageUrl.text.toString() // Ambil URL gambar

        // Validasi input
        if (recipeName.isEmpty() || calories <= 0 || duration.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek Recipe tanpa id
        val recipe = Recipe(name = recipeName, imageUrl = imageUrl, calories = calories, cookingTime = duration)

        // Kirim data ke API
        Client.getInstance().addRecipe(recipe).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Recipe added successfully!", Toast.LENGTH_SHORT).show()
                    // Reset input fields
                    resetInputFields()
                } else {
                    Toast.makeText(requireContext(), "Failed to add recipe: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetInputFields() {
        binding.editTextRecipeName.text.clear()
        binding.editTextCalories.text.clear()
        binding.editTextDuration.text.clear()
        binding.editTextImageUrl.text.clear()
        binding.imageViewRecipe.setImageDrawable(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
