package com.example.uas_pppb

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        FILE_NAME, Context.MODE_PRIVATE
    )

    companion object {
        private const val FILE_NAME = "user_prefs"
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }

        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NAME = "name"
    }

    fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "").orEmpty()
    }

    fun saveName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, name)
        editor.apply()
    }

    fun getName(): String {
        return sharedPreferences.getString(KEY_NAME, "").orEmpty()
    }

    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "").orEmpty()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
