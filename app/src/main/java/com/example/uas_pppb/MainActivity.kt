package com.example.uas_pppb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_saved_recipes -> {
                    navController.navigate(R.id.savedRecipesFragment) // Buat fragment ini nanti
                    true
                }
                R.id.nav_add_recipes -> {
                    navController.navigate(R.id.addRecipeFragment)
                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.profileFragment) // Pastikan fragment ini ada
                    true
                }
                else -> false
            }
        }
    }
}
