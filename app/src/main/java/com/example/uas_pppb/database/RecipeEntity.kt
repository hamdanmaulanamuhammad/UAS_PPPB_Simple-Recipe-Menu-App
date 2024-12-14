package com.example.uas_pppb.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) @NonNull val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "calories") val calories: Int,
    @ColumnInfo(name = "cooking_time") val cookingTime: String
)
