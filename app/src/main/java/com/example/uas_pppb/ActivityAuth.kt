package com.example.uas_pppb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_pppb.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator

class ActivityAuth : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(LoginFragment(), "Login")
        adapter.addFragment(RegisterFragment(), "Register")
        binding.viewPager.adapter = adapter

        // Menghubungkan TabLayout dengan ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }

    // Metode untuk berpindah ke tab Login
    fun moveToLoginTab() {
        binding.viewPager.currentItem = 0 // Pindah ke tab Login (indeks 0)
    }
}

