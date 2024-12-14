package com.example.uas_pppb.network

import com.example.uas_pppb.model.Recipe
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApi {
    @GET("dmn6c/recipe")
    fun getRecipes(): Call<List<Recipe>>

    @POST("dmn6c/recipe")
    fun addRecipe(@Body recipe: Recipe): Call<Void>

    @DELETE("dmn6c/recipe/{id}")
    fun deleteRecipe(@Path("id") recipeId: String): Call<Void>
}
