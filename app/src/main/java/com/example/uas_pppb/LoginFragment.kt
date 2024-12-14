package com.example.uas_pppb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_pppb.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        prefManager = PrefManager.getInstance(requireContext())

        // Cek status login saat fragment dibuat
        if (prefManager.isLoggedIn()) {
            moveToMainActivity()
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.loginEditTextEmail.text.toString()
            val password = binding.loginEditTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Validasi kredensial
                val savedEmail = prefManager.getEmail()
                val savedPassword = prefManager.getPassword()

                if (email == savedEmail && password == savedPassword) {
                    // Login berhasil
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    prefManager.saveLoginStatus(true) // Simpan status login
                    moveToMainActivity()
                } else {
                    Toast.makeText(requireContext(), "Email or password is incorrect", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun moveToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
