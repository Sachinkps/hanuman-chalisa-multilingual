package com.sachinkps.hanumanChalisa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sachinkps.hanumanChalisa.data.models.VerseRaw
import com.sachinkps.hanumanChalisa.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val selectedLang by viewModel.selectedLangCode.collectAsState()
    val verses by viewModel.verses.collectAsState()
    val title by viewModel.title.collectAsState()
    val showTranslit by viewModel.showTransliteration.collectAsState()
    var showLangDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleTransliteration() }) {
                        Icon(Icons.Default.Settings, contentDescription = "Toggle Transliteration")
                    }
                    IconButton(onClick = { showLangDialog = true }) {
                        Icon(Icons.Default.Language, contentDescription = "Select Language")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Header
            item {
                Text(
                    text = "॥ श्री हनुमान चालीसा ॥",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            itemsIndexed(verses) { index, verse ->
                VerseCard(
                    verse = verse,
                    langCode = selectedLang,
                    showTransliteration = showTranslit,
                    verseNumber = if (verse.type == "chaupai") {
                        val chaupaiIndex = verses.take(index + 1).count { it.type == "chaupai" }
                        chaupaiIndex
                    } else null
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "॥ इति श्री हनुमान चालीसा समाप्त ॥",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showLangDialog) {
        LanguageSelectionDialog(
            languages = viewModel.supportedLanguages,
            selectedCode = selectedLang,
            onSelect = { code ->
                viewModel.setLanguage(code)
                showLangDialog = false
            },
            onDismiss = { showLangDialog = false }
        )
    }
}

@Composable
fun VerseCard(
    verse: VerseRaw,
    langCode: String,
    showTransliteration: Boolean,
    verseNumber: Int?
) {
    val isDoha = verse.type == "doha"
    val bgColor = if (isDoha)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isDoha) {
                Text(
                    text = "दोहा",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            } else if (verseNumber != null) {
                Text(
                    text = "चौपाई $verseNumber",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Hindi always shown
            Text(
                text = verse.hindi,
                fontSize = 18.sp,
                fontWeight = if (isDoha) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 28.sp
            )

            // Transliteration
            if (showTransliteration && langCode != "hi") {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = verse.transliteration,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }

            // Translation
            if (langCode != "hi") {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = verse.getTextForLanguage(langCode),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                )
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    languages: List<com.sachinkps.hanumanChalisa.data.models.Language>,
    selectedCode: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Language / भाषा चुनें") },
        text = {
            LazyColumn {
                items(languages.size) { index ->
                    val lang = languages[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(lang.code) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = lang.code == selectedCode,
                            onClick = { onSelect(lang.code) }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = lang.nativeName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = lang.name,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
