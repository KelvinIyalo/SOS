package com.example.sosapplication.utils

import android.content.Context
import com.example.sosapplication.model.EmergencyNumber
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesHelper {
    private const val PREF_NAME = "UserSharedPreferences"
    private const val KEY_USER_LIST = "userList"

    private val gson = Gson()

    fun saveUserList(context: Context, userList: List<EmergencyNumber>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val userListJson = gson.toJson(userList)

        editor.putString(KEY_USER_LIST, userListJson)
        editor.apply()
    }

    fun isSharedPreferencesEmpty(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        return allEntries.isEmpty()
    }
    fun getUserList(context: Context): List<EmergencyNumber> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userListJson = sharedPreferences.getString(KEY_USER_LIST, null)

        return if (userListJson != null) {
            gson.fromJson(userListJson, object : TypeToken<List<EmergencyNumber>>() {}.type)
        } else {
            emptyList()
        }
    }
}