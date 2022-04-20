package com.kkobook.qrcode.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kkobook.qrcode.model.History

class QRSharedPreferences(context: Context) {
    private val prefsFilename = "prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    private var qrCodeLists: MutableList<History>? = null
    private val key = "qrCodeLists"

    fun saveQRCodes(message: String, currentTimeMillis: Long) {
        val json = prefs.getString(key, "")
        val type = object : TypeToken<MutableList<History>>() {}.type
        qrCodeLists = Gson().fromJson<MutableList<History>>(json, type) ?: mutableListOf()
        qrCodeLists!!.add(History(message, currentTimeMillis))
        prefs.edit().putString(key, Gson().toJson(qrCodeLists)).apply()
    }

    fun getQRCodes(): MutableList<History> {
        val json = prefs.getString(key, "")
        val type = object : TypeToken<MutableList<History>>() {}.type
        return Gson().fromJson<MutableList<History>>(json, type) ?: mutableListOf()
    }

    fun deleteQRCodes() {
        prefs.edit().putString(key, "").apply()
    }

//    var myEditText: String?
//        get() = prefs.getString(key, "")
//        set(value) = prefs.edit().putString(key, value).apply()
}