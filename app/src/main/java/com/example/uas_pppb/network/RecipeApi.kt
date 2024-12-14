package com.example.uas_pppb.network

import com.example.uas_pppb.model.Recipe
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RecipeApi {
    @GET("dmn6c/recipe")
    fun getRecipes(): Call<List<Map<String, Any>>>

    @POST("dmn6c/recipe")
    fun addRecipe(@Body recipe: Recipe): Call<Void>
}
