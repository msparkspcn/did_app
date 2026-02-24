package com.secta9ine.didapp.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import java.io.File

@Composable
fun ImageContent(
    contentUrl: String,
    localPath: String?,
    modifier: Modifier = Modifier
) {
    val model = if (localPath != null) {
        File(localPath)
    } else {
        contentUrl
    }

    AsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}
