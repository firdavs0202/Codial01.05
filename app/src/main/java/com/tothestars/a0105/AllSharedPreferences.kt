package com.tothestars.a0105

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AllSharedPreferences {
    private const val NAME = "contacts"
    private const val MODE = Context.MODE_PRIVATE

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var contacts: ArrayList<ContactModel>
        get() = toMap(sharedPreferences.getString(NAME, toStr(Contacts.contacts))!!)
        set(value) = sharedPreferences.edit {
            it.putString(NAME, toStr(value))
        }

    private fun toMap(gsonString: String): ArrayList<ContactModel> {
        val type = object : TypeToken<ArrayList<ContactModel>>() {}.type
        return Gson().fromJson(gsonString, type)
    }

    private fun toStr(list: ArrayList<ContactModel>): String = Gson().toJson(list)
}