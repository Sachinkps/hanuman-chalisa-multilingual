package com.sachinkps.hanumanChalisa.data.models

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val script: String
)

data class Verse(
    val id: String,
    val type: String, // "doha" or "chaupai"
    val number: Int,
    val text: String,
    val transliteration: String
)

data class ChaLisaContent(
    val titleTranslations: Map<String, String>,
    val openingDohas: List<VerseRaw>,
    val chaupais: List<VerseRaw>,
    val closingDoha: VerseRaw
)

data class VerseRaw(
    val id: String,
    val type: String,
    val hindi: String,
    val transliteration: String,
    val english: String,
    val malayalam: String,
    val tamil: String,
    val telugu: String,
    val kannada: String,
    val bengali: String,
    val gujarati: String,
    val marathi: String,
    val punjabi: String
) {
    fun getTextForLanguage(langCode: String): String = when (langCode) {
        "hi" -> hindi
        "en" -> english
        "ml" -> malayalam
        "ta" -> tamil
        "te" -> telugu
        "kn" -> kannada
        "bn" -> bengali
        "gu" -> gujarati
        "mr" -> marathi
        "pa" -> punjabi
        else -> hindi
    }
}

val SUPPORTED_LANGUAGES = listOf(
    Language("hi", "Hindi", "हिन्दी", "Devanagari"),
    Language("en", "English", "English", "Latin"),
    Language("ml", "Malayalam", "മലയാളം", "Malayalam"),
    Language("ta", "Tamil", "தமிழ்", "Tamil"),
    Language("te", "Telugu", "తెలుగు", "Telugu"),
    Language("kn", "Kannada", "ಕನ್ನಡ", "Kannada"),
    Language("bn", "Bengali", "বাংলা", "Bengali"),
    Language("gu", "Gujarati", "ગુજરાતી", "Gujarati"),
    Language("mr", "Marathi", "मराठी", "Devanagari"),
    Language("pa", "Punjabi", "ਪੰਜਾਬੀ", "Gurmukhi")
)
