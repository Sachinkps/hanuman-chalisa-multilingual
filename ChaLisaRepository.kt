package com.sachinkps.hanumanChalisa.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sachinkps.hanumanChalisa.data.models.VerseRaw

class ChaLisaRepository(private val context: Context) {

    private val gson = Gson()
    private var cachedData: JsonObject? = null

    private fun loadJson(): JsonObject {
        if (cachedData != null) return cachedData!!
        val json = context.assets.open("verses/hanuman_chalisa.json")
            .bufferedReader().use { it.readText() }
        cachedData = gson.fromJson(json, JsonObject::class.java)
        return cachedData!!
    }

    fun getTitle(langCode: String): String {
        val titles = loadJson().getAsJsonObject("title")
        return when (langCode) {
            "hi" -> titles["hindi"].asString
            "en" -> titles["english"].asString
            "ml" -> titles["malayalam"].asString
            "ta" -> titles["tamil"].asString
            "te" -> titles["telugu"].asString
            "kn" -> titles["kannada"].asString
            "bn" -> titles["bengali"].asString
            "gu" -> titles["gujarati"].asString
            "mr" -> titles["marathi"].asString
            "pa" -> titles["punjabi"].asString
            else -> titles["hindi"].asString
        }
    }

    fun getOpeningDohas(): List<VerseRaw> {
        val array = loadJson().getAsJsonArray("doha_opening")
        return array.map { gson.fromJson(it, VerseRaw::class.java) }
    }

    fun getChaupais(): List<VerseRaw> {
        val array = loadJson().getAsJsonArray("chaupai")
        return array.mapIndexed { index, element ->
            val obj = element.asJsonObject
            VerseRaw(
                id = (index + 1).toString(),
                type = "chaupai",
                hindi = obj["hindi"]?.asString ?: "",
                transliteration = obj["transliteration"]?.asString ?: "",
                english = obj["english"]?.asString ?: "",
                malayalam = obj["malayalam"]?.asString ?: "",
                tamil = obj["tamil"]?.asString ?: "",
                telugu = obj["telugu"]?.asString ?: "",
                kannada = obj["kannada"]?.asString ?: "",
                bengali = obj["bengali"]?.asString ?: "",
                gujarati = obj["gujarati"]?.asString ?: "",
                marathi = obj["marathi"]?.asString ?: "",
                punjabi = obj["punjabi"]?.asString ?: ""
            )
        }
    }

    fun getClosingDoha(): VerseRaw {
        val obj = loadJson().getAsJsonObject("doha_closing")
        return gson.fromJson(obj, VerseRaw::class.java)
    }

    fun getAllVerses(): List<VerseRaw> {
        return getOpeningDohas() + getChaupais() + listOf(getClosingDoha())
    }
}
