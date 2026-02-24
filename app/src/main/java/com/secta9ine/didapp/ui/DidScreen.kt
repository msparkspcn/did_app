package com.secta9ine.didapp.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.secta9ine.didapp.data.local.DidEntity
import com.secta9ine.didapp.ui.components.ImageContent
import com.secta9ine.didapp.ui.components.TextContent
import com.secta9ine.didapp.ui.components.VideoContent
import com.secta9ine.didapp.ui.viewmodel.DidViewModel
import kotlinx.coroutines.delay

// LEGACY (v1): Single-zone full-screen renderer.
// Keep for rollback/reference. The v2 dynamic multi-zone renderer is
// com.secta9ine.didapp.v2.ui.DynamicDidScreenV2.
@Composable
fun DidScreen(viewModel: DidViewModel) {
    val items by viewModel.didItems.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }

    if (items.isEmpty()) {
        TextContent(text = "No content available")
    } else {
        LaunchedEffect(items.size, currentIndex) {
            if (currentIndex !in items.indices) {
                currentIndex = currentIndex.mod(items.size)
            }
        }
        val safeIndex = currentIndex.coerceIn(0, items.lastIndex)
        val currentItem = items[safeIndex]

        fun moveToNext() {
            currentIndex = (currentIndex + 1) % items.size
        }

        // Auto-rolling logic for static content (IMAGE, TEXT)
        LaunchedEffect(currentIndex, currentItem.type) {
            if (currentItem.type != "VIDEO") {
                delay(10000) // 10 seconds display for static content
                moveToNext()
            }
        }

        AnimatedContent(
            targetState = safeIndex,
            transitionSpec = {
                // Rolling Up Effect: Next item comes from bottom, current item exits top
                slideInVertically { height -> height } + fadeIn() togetherWith
                slideOutVertically { height -> -height } + fadeOut()
            },
            label = "ContentRolling",
            modifier = Modifier.fillMaxSize()
        ) { index ->
            val item = items[index.coerceIn(0, items.lastIndex)]
            ContentItem(
                item = item,
                onVideoEnd = { moveToNext() }
            )
        }
    }
}

@Composable
fun ContentItem(item: DidEntity, onVideoEnd: () -> Unit) {
    when (item.type) {
        "IMAGE" -> ImageContent(contentUrl = item.content, localPath = item.localPath)
        "VIDEO" -> VideoContent(contentUrl = item.content, localPath = item.localPath, onVideoEnd = onVideoEnd)
        "TEXT" -> TextContent(text = item.content)
    }
}
