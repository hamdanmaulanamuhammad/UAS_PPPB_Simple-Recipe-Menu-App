package com.example.uas_pppb.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_pppb.database.RecipeEntity

class SavedRecipeAdapter(private val savedRecipes: List<RecipeEntity>) : RecyclerView.Adapter<SavedRecipeAdapter.SavedRecipeViewHolder>() {

    class SavedRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(android.R.id.text1)
        // Anda bisa menambahkan lebih banyak TextView di sini untuk menampilkan informasi lain dari RecipeEntity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return SavedRecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedRecipeViewHolder, position: Int) {
        val recipe = savedRecipes[position]
        holder.recipeName.text = recipe.name // Ganti dengan atribut yang sesuai dari RecipeEntity
        // Anda bisa menambahkan lebih banyak binding di sini untuk menampilkan informasi lain
    }

    override fun getItemCount(): Int {
        return savedRecipes.size
    }
}
