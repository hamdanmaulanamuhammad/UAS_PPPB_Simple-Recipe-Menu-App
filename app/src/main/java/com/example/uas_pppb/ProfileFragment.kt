package com.example.uas_pppb.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uas_pppb.ActivityAuth
import com.example.uas_pppb.PrefManager
import com.example.uas_pppb.R
import com.example.uas_pppb.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Inisialisasi PrefManager
        val prefManager = PrefManager.getInstance(requireContext())

        // Ambil data dari PrefManager
        val name = prefManager.getName() // Ambil username
        val email = prefManager.getEmail() // Ambil email

        // Tampilkan data di UI
        binding.textViewName.text = name
        binding.textViewEmail.text = email

        // Debugging
        Log.d("ProfileFragment", "Username: $name, Email: $email")

        // Logout button
        binding.buttonLogout.setOnClickListener {
            // Hapus data dari PrefManager
            prefManager.clear()

            // Navigasi ke ActivityAuth menggunakan Intent
            val intent = Intent(requireActivity(), ActivityAuth::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
