package com.example.uas_pppb.model

data class Recipe(
    val _id: String? = null,
    val name: String,
    val imageUrl: String,
    val calories: Int,
    val cookingTime: String
)
