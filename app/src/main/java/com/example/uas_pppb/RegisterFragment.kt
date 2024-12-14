package com.example.uas_pppb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_pppb.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        prefManager = PrefManager.getInstance(requireContext())

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Validasi input
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Hapus data pengguna sebelumnya
                prefManager.clear()

                // Simpan data ke PrefManager
                prefManager.saveName(name) // Simpan username
                prefManager.saveEmail(email) // Simpan email
                prefManager.savePassword(password) // Simpan password

                // Tampilkan Toast dan pindah ke tab Login
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                moveToLoginTab()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun moveToLoginTab() {
        (activity as? ActivityAuth)?.moveToLoginTab()
    }
}
